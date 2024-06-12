import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import axios from 'axios'

@Component({
  selector: 'app-add-customers',
  templateUrl: './add-customers.component.html',
  styleUrls: ['./add-customers.component.css']
})
export class AddCustomersComponent implements OnInit {
  formGroup!: FormGroup

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      customerName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(32),
          Validators.pattern(/^([^0-9]*)$/)
        ]
      ],
      customerAddress: [
        '',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(70)
        ]
      ],
      customerPhone: ['', [Validators.pattern(/^0\d{9,12}$/)]],
      file: [null]
    })
  }

  onFileChange(event: Event): void {
    const inputElement = event.target as HTMLInputElement
    if (inputElement.files && inputElement.files.length > 0) {
      this.formGroup.controls['file'].setValue(inputElement.files[0])
    }
  }

  async onSubmitForm(): Promise<void> {
    if (this.formGroup.invalid) {
      console.log('Formulir tidak valid')
      return
    }

    try {
      const formData = new FormData()
      formData.append('customerName', this.formGroup.value.customerName)
      formData.append('customerAddress', this.formGroup.value.customerAddress)
      formData.append('customerPhone', this.formGroup.value.customerPhone)
      formData.append('file', this.formGroup.value.file)

      const response = await axios.post(
        'http://localhost:8080/api/customers',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      console.log('Customer added:', response.data)
      alert('Customer berhasil ditambahkan')
      this.router.navigate([''])
    } catch (error) {
      console.error('Error add Customer:', error)

      if (
        error instanceof axios.AxiosError &&
        error.response &&
        error.response.data === 'Nomor telepon sudah tersedia'
      ) {
        alert('Nomor telepon sudah tersedia')
      } else {
        alert('Gagal menambahkan Customer')
      }
    }
  }
}
