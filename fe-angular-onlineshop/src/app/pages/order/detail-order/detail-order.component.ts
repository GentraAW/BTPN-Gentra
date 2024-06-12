import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import axios from 'axios'
@Component({
  selector: 'app-detail-order',
  templateUrl: './detail-order.component.html',
  styleUrl: './detail-order.component.css'
})
export class DetailOrderComponent implements OnInit {
  order: any = {}
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  goToPagesOrder(): void {
    this.router.navigate(['/order'])
  }

  ngOnInit(): void {
    const orderId = this.route.snapshot.paramMap.get('orderId')
    if (orderId) {
      this.getOrderDetail(orderId)
    } else {
      this.router.navigate(['/order'])
    }
  }

  async getOrderDetail(orderId: string): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/orders/${orderId}`
      )
      this.order = response.data
    } catch (error) {
      console.error('Error fetching customer detail:', error)
      this.router.navigate(['/order'])
    }
  }
}
