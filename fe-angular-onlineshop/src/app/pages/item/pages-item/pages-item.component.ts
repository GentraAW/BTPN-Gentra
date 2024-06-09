import { Component, OnInit } from '@angular/core'
import axios from 'axios'
import { Router } from '@angular/router'

@Component({
  selector: 'app-pages-item',
  templateUrl: './pages-item.component.html',
  styleUrl: './pages-item.component.css'
})
export class PagesItemComponent implements OnInit {
  items: any[] = []
  totalPages: number = 0
  currentPage: number = 0
  pageSize: number = 2
  searchItemName: string = ''
  sortDirection: string = 'asc'

  constructor(private router: Router) {}

  goDetailItem(itemId: number): void {
    this.router.navigate(['/detail-item', itemId])
  }

  goAddItem(): void {
    this.router.navigate(['/add-item'])
  }

  goEditItem(itemId: number): void {
    this.router.navigate(['/edit-item', itemId])
  }

  ngOnInit(): void {
    this.getItems()
  }

  async getItems(page: number = 0): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/items?page=${page}&size=${this.pageSize}&itemName=${this.searchItemName}&direction=${this.sortDirection}`
      )
      this.items = response.data.content
      this.totalPages = response.data.totalPages
      this.currentPage = response.data.number + 1 // currentPage dimulai dari 1
    } catch (error) {
      console.error('Error fetching items:', error)
    }
  }

  async deleteItem(itemId: number): Promise<void> {
    try {
      const response = await axios.delete(
        `http://localhost:8080/api/items/${itemId}`
      )
      console.log('Item deleted:', response.data)
      alert('Item berhasil dihapus')
      // Refresh data setelah menghapus pelanggan
      this.getItems(0)
    } catch (error) {
      console.error('Error deleting Item:', error)
      alert('Gagal menghapus Item')
    }
  }

  onPageChange(page: number): void {
    this.getItems(page - 1) // Karena pagination dimulai dari 1, sedangkan index page dimulai dari 0
  }

  searchItems(): void {
    this.getItems()
  }

  sortItems(): void {
    this.getItems()
  }
}
