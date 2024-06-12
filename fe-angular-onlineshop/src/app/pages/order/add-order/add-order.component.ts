import { Component, OnInit } from '@angular/core'
import { Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import axios from 'axios'

@Component({
  selector: 'app-add-order',
  templateUrl: './add-order.component.html',
  styleUrls: ['./add-order.component.css']
})
export class AddOrderComponent implements OnInit {
  customers: any[] = []
  items: any[] = []
  formGroup!: FormGroup

  constructor(
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  goToPagesOrder(): void {
    this.router.navigate(['/order'])
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      customerId: [
        '',
        [Validators.required, Validators.pattern(/^[1-9][0-9]*$/)]
      ],
      itemId: ['', [Validators.required, Validators.pattern(/^[1-9][0-9]*$/)]],
      quantity: ['', [Validators.required, Validators.pattern(/^[1-9][0-9]*$/)]]
    })

    // Fetch items dan customers
    this.getItems()
    this.getCustomers()
  }

  async getItems(): Promise<void> {
    try {
      const response = await axios.get(`http://localhost:8080/api/items?size=`)
      this.items = response.data.content
    } catch (error) {
      console.error('Error fetching items:', error)
    }
  }

  async getCustomers(): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/customers?size=`
      )
      this.customers = response.data.content
    } catch (error) {
      console.error('Error fetching customers:', error)
    }
  }

  async onSubmitForm(): Promise<void> {
    if (this.formGroup.invalid) {
      console.log('Formulir tidak valid')
      return
    }

    try {
      const formData = new FormData()
      formData.append('customerId', this.formGroup.value.customerId)
      formData.append('itemId', this.formGroup.value.itemId)
      formData.append('quantity', this.formGroup.value.quantity)

      const response = await axios.post(
        'http://localhost:8080/api/orders',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      console.log('Order added:', response.data)
      alert('Order berhasil ditambahkan')
      this.router.navigate(['/order'])
    } catch (error) {
      console.error('Error adding Order:', error)
    }
  }
}
