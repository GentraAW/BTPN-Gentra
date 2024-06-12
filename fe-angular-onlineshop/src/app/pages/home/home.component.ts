import { Component, OnInit } from '@angular/core'
import axios from 'axios'
import { Router } from '@angular/router'
import {
  faEye,
  faEdit,
  faTrash,
  faCheck,
  faTimes
} from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customers: any[] = []
  totalPages: number = 0
  currentPage: number = 0
  pageSize: number = 4
  searchCustomerName: string = ''
  sortDirection: string = 'asc'
  isModalOpen: boolean = false
  customerNameToDelete: string = ''
  customerIdToDelete: number = 0

  constructor(private router: Router) {}

  faEye = faEye
  faEdit = faEdit
  faTrash = faTrash
  faCheck = faCheck
  faTimes = faTimes

  goDetailCustomer(customerId: number): void {
    this.router.navigate(['/detail-customer', customerId])
  }

  goAddCustomer(): void {
    this.router.navigate(['/add-customer'])
  }

  goEditCustomer(customerId: number): void {
    this.router.navigate(['/edit-customer', customerId])
  }

  ngOnInit(): void {
    this.getCustomers()
  }

  async getCustomers(page: number = 0): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/customers?page=${page}&size=${this.pageSize}&customerName=${this.searchCustomerName}&direction=${this.sortDirection}`
      )
      this.customers = response.data.content
      this.totalPages = response.data.totalPages
      this.currentPage = response.data.number + 1
    } catch (error) {
      console.error('Error fetching customers:', error)
    }
  }

  openDeleteConfirmationModal(customerId: number, customerName: string): void {
    this.customerIdToDelete = customerId
    this.customerNameToDelete = customerName
    this.isModalOpen = true
  }

  closeDeleteConfirmationModal(): void {
    this.isModalOpen = false
  }

  async confirmDeleteCustomer(): Promise<void> {
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/customers/${this.customerIdToDelete}`
      )
      console.log('Customer deleted:', response.data)
      alert('Customer berhasil dihapus')
      this.getCustomers(0)
    } catch (error) {
      console.error('Error deleting customer:', error)
      alert('Gagal menghapus Customer')
    } finally {
      this.closeDeleteConfirmationModal()
    }
  }

  onPageChange(page: number): void {
    this.getCustomers(page - 1)
  }

  searchCustomers(): void {
    this.getCustomers()
  }

  sortCustomers(): void {
    this.getCustomers()
  }
}
