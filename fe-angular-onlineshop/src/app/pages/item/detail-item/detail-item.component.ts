import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import axios from 'axios'
@Component({
  selector: 'app-detail-item',
  templateUrl: './detail-item.component.html',
  styleUrl: './detail-item.component.css'
})
export class DetailItemComponent implements OnInit {
  item: any = {}
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  goToPagesItem(): void {
    this.router.navigate(['/item'])
  }

  ngOnInit(): void {
    const itemId = this.route.snapshot.paramMap.get('itemId')
    if (itemId) {
      this.getItemDetail(itemId)
    } else {
      this.router.navigate(['/item'])
    }
  }

  async getItemDetail(itemId: string): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/items/${itemId}`
      )
      this.item = response.data
    } catch (error) {
      console.error('Error fetching customer detail:', error)
      this.router.navigate(['/item'])
    }
  }
}
