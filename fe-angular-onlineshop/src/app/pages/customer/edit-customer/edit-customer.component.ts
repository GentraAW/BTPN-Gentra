import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import axios from 'axios'

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {
  customerId: number = 0
  formGroup!: FormGroup

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.customerId = Number(this.route.snapshot.paramMap.get('customerId'))

    this.formGroup = this.formBuilder.group({
      customerName: [
        '',
        [Validators.required, Validators.minLength(1), Validators.maxLength(32)]
      ],
      customerPhone: ['', [Validators.pattern(/^0\d{9,12}$/)]],
      isActive: [false],
      lastOrderDate: [null],
      file: [null]
    })

    this.loadCustomerData()
  }

  async loadCustomerData(): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/customers/${this.customerId}`
      )
      const customerData = response.data

      this.formGroup.patchValue({
        customerName: customerData.customerName,
        customerPhone: customerData.customerPhone,
        isActive: customerData.isActive,
        lastOrderDate: customerData.lastOrderDate
      })
    } catch (error) {
      console.error('Error fetching customer data:', error)
    }
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
      formData.append('customerName', this.formGroup.get('customerName')?.value)
      formData.append(
        'customerPhone',
        this.formGroup.get('customerPhone')?.value
      )
      formData.append(
        'isActive',
        this.formGroup.get('isActive')?.value.toString()
      )
      formData.append(
        'lastOrderDate',
        this.formGroup.get('lastOrderDate')?.value ?? ''
      )
      formData.append('file', this.formGroup.get('file')?.value)

      const response = await axios.put(
        `http://localhost:8080/api/customers/${this.customerId}`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )
      console.log('Customer updated:', response.data)
      alert('Customer berhasil ditambahkan')
      this.router.navigate([''])
    } catch (error) {
      console.error('Error updating customer:', error)
      alert('Customer gagal di update')
    }
  }
}
