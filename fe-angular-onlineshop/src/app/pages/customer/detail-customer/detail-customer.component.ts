import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import axios from 'axios'

@Component({
  selector: 'app-detail-customer',
  templateUrl: './detail-customer.component.html',
  styleUrls: ['./detail-customer.component.css']
})
export class DetailCustomerComponent implements OnInit {
  customer: any = {}

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  goToHome(): void {
    this.router.navigate([''])
  }

  ngOnInit(): void {
    const customerId = this.route.snapshot.paramMap.get('customerId')
    if (customerId) {
      this.getCustomerDetail(customerId)
    } else {
      this.router.navigate(['/']) // Navigasi ke halaman home jika customerId null
    }
  }

  async getCustomerDetail(customerId: string): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/customers/${customerId}`
      )
      this.customer = response.data
    } catch (error) {
      console.error('Error fetching customer detail:', error)
      this.router.navigate(['/']) // Navigasi ke halaman home jika terjadi kesalahan saat mengambil data
    }
  }
}
