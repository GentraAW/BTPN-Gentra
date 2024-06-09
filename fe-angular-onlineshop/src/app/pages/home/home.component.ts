import { Component, OnInit } from '@angular/core'
import axios from 'axios'
import { Router } from '@angular/router'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  customers: any[] = []
  totalPages: number = 0
  currentPage: number = 0
  pageSize: number = 2
  searchCustomerName: string = '' // Tambahkan properti searchCustomerName
  sortDirection: string = 'asc' // Tambahkan properti sortDirection

  constructor(private router: Router) {}

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
      this.currentPage = response.data.number + 1 // currentPage dimulai dari 1
    } catch (error) {
      console.error('Error fetching customers:', error)
    }
  }

  async deleteCustomer(customerId: number): Promise<void> {
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/customers/${customerId}`
      )
      console.log('Customer deleted:', response.data)
      alert('Customer berhasil dihapus')
      // Refresh data setelah menghapus
      this.getCustomers(0)
    } catch (error) {
      console.error('Error deleting customer:', error)
      alert('Gagal menghapus Customer')
    }
  }

  onPageChange(page: number): void {
    this.getCustomers(page - 1) // Karena pagination dimulai dari 1, sedangkan index page dimulai dari 0
  }

  searchCustomers(): void {
    this.getCustomers()
  }

  sortCustomers(): void {
    this.getCustomers()
  }
}
