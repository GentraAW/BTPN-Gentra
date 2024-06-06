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

  constructor(private router: Router) {}

  goToDetail(customerId: number): void {
    this.router.navigate(['/detail-customer', customerId])
  }

  ngOnInit(): void {
    this.getCustomers()
  }

  async getCustomers(page: number = 0): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/customers?page=${page}&size=${this.pageSize}`
      )
      this.customers = response.data.content
      this.totalPages = response.data.totalPages
      this.currentPage = response.data.number + 1 // currentPage dimulai dari 1
    } catch (error) {
      console.error('Error fetching customers:', error)
    }
  }

  onPageChange(page: number): void {
    this.getCustomers(page - 1) // Karena pagination dimulai dari 1, sedangkan index page dimulai dari 0
  }
}
