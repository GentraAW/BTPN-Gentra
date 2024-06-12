import { Component, OnInit } from '@angular/core'
import axios from 'axios'
import { Router } from '@angular/router'
import {
  faEye,
  faEdit,
  faTrash,
  faCheck,
  faTimes,
  faFileAlt
} from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-pages-order',
  templateUrl: './pages-order.component.html',
  styleUrl: './pages-order.component.css'
})
export class PagesOrderComponent implements OnInit {
  orders: any[] = []
  totalPages: number = 0
  currentPage: number = 0
  pageSize: number = 7
  searchOrderCustomerName: string = ''
  sortDirection: string = 'asc'
  isModalOpen: boolean = false
  orderNameToDelete: string = ''
  orderIdToDelete: number = 0

  constructor(private router: Router) {}

  faEye = faEye
  faEdit = faEdit
  faTrash = faTrash
  faCheck = faCheck
  faTimes = faTimes
  faFileAlt = faFileAlt

  goDetailOrder(orderId: number): void {
    this.router.navigate(['/detail-order', orderId])
  }

  goAddOrder(): void {
    this.router.navigate(['/add-order'])
  }

  goEditOrder(orderId: number): void {
    this.router.navigate(['/edit-order', orderId])
  }

  ngOnInit(): void {
    this.getOrders()
  }

  async getOrders(page: number = 0): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/orders?page=${page}&size=${this.pageSize}&customerName=${this.searchOrderCustomerName}&direction=${this.sortDirection}`
      )
      this.orders = response.data.content
      this.totalPages = response.data.totalPages
      this.currentPage = response.data.number + 1
    } catch (error) {
      console.error('Error fetching orders:', error)
    }
  }

  openDeleteConfirmationModal(orderId: number, orderCode: string): void {
    this.orderIdToDelete = orderId
    this.orderNameToDelete = orderCode
    this.isModalOpen = true
  }

  closeDeleteConfirmationModal(): void {
    this.isModalOpen = false
  }

  async confirmDeleteOrder(): Promise<void> {
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/orders/${this.orderIdToDelete}`
      )
      console.log('orders deleted:', response.data)
      alert('orders berhasil dihapus')
      this.getOrders(0)
    } catch (error) {
      console.error('Error deleting orders:', error)
      alert('Gagal menghapus orders')
    } finally {
      this.closeDeleteConfirmationModal()
    }
  }

  onPageChange(page: number): void {
    this.getOrders(page - 1) // Karena pagination dimulai dari 1, sedangkan index page dimulai dari 0
  }

  searchOrders(): void {
    this.getOrders()
  }

  sortOrders(): void {
    this.getOrders()
  }

  formatRupiah(amount: number): string {
    return new Intl.NumberFormat('id-ID', {
      style: 'currency',
      currency: 'IDR'
    }).format(amount)
  }

  async downloadReport(): Promise<void> {
    try {
      const response = await axios.get(
        'http://localhost:8080/api/reports/orders?format=pdf',
        {
          responseType: 'blob'
        }
      )

      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'orders_report.pdf')
      document.body.appendChild(link)
      link.click()
      link.remove()
    } catch (error) {
      console.error('Error downloading report:', error)
      alert('Gagal mengunduh laporan')
    }
  }
}
