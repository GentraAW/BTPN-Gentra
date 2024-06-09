import { Component, OnInit } from '@angular/core'
import axios from 'axios'
import { Router } from '@angular/router'

@Component({
  selector: 'app-pages-order',
  templateUrl: './pages-order.component.html',
  styleUrl: './pages-order.component.css'
})
// implements OnInit
export class PagesOrderComponent {
  // orders: any[] = []
  // totalPages: number = 0
  // currentPage: number = 0
  // pageSize: number = 2
  // searchItemName: string = ''
  // sortDirection: string = 'asc'
  // constructor(private router: Router) {}
  // goDetailOrder(orderId: number): void {
  //   this.router.navigate(['/detail-order', orderId])
  // }
  // goAddOrder(): void {
  //   this.router.navigate(['/add-order'])
  // }
  // goEditOrder(orderId: number): void {
  //   this.router.navigate(['/edit-order', orderId])
  // }
  // ngOnInit(): void {
  //   this.getOrders()
  // }
  // async getOrders(page: number = 0): Promise<void> {
  //   try {
  //     const response = await axios.get(
  //       `http://localhost:8080/api/orders?page=${page}&size=${this.pageSize}&customerName=${this.searchCustomerName}&direction=${this.sortDirection}`
  //     )
  //     this.customers = response.data.content
  //     this.totalPages = response.data.totalPages
  //     this.currentPage = response.data.number + 1 // currentPage dimulai dari 1
  //   } catch (error) {
  //     console.error('Error fetching customers:', error)
  //   }
  // }
}
