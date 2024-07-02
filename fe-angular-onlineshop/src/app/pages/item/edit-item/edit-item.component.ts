import { Component, OnInit } from '@angular/core'
import { ActivatedRoute, Router } from '@angular/router'
import { FormBuilder, FormGroup, Validators } from '@angular/forms'
import axios from 'axios'

@Component({
  selector: 'app-edit-item',
  templateUrl: './edit-item.component.html',
  styleUrls: ['./edit-item.component.css']
})
export class EditItemComponent implements OnInit {
  itemId: number = 0
  formGroup!: FormGroup

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  goToPagesItem(): void {
    this.router.navigate(['/item'])
  }

  ngOnInit() {
    this.itemId = Number(this.route.snapshot.paramMap.get('itemId'))

    this.formGroup = this.formBuilder.group({
      itemName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(32),
          Validators.pattern(/^([^0-9]*)$/)
        ]
      ],
      itemCode: [
        '',
        [Validators.required, Validators.minLength(5), Validators.maxLength(32)]
      ],
      stock: ['', [Validators.maxLength(10), Validators.pattern(/^[0-9]+$/)]],
      price: [
        '',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(32),
          Validators.pattern(/^[0-9]+$/)
        ]
      ],
      lastReStock: [null]
    })

    this.loadItemData()
  }

  async loadItemData(): Promise<void> {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/items/${this.itemId}`
      )
      const itemData = response.data

      const lastReStock = itemData.lastReStock
        ? new Date(itemData.lastReStock).toISOString().slice(0, 10)
        : null

      this.formGroup.patchValue({
        itemName: itemData.itemName,
        itemCode: itemData.itemCode,
        stock: itemData.stock,
        price: itemData.price,
        lastReStock: lastReStock
      })
    } catch (error) {
      console.error('Error fetching item data:', error)
    }
  }

  async onSubmitForm(): Promise<void> {
    if (this.formGroup.invalid) {
      console.log('Formulir tidak valid')
      return
    }

    try {
      const formData = new FormData()
      formData.append('itemName', this.formGroup.get('itemName')?.value)
      formData.append('itemCode', this.formGroup.get('itemCode')?.value)
      formData.append('stock', this.formGroup.get('stock')?.value)
      formData.append('price', this.formGroup.get('price')?.value)

      let lastReStock = this.formGroup.get('lastReStock')?.value

      if (lastReStock) {
        const currentDate = new Date(lastReStock)
        const now = new Date()

        currentDate.setHours(now.getHours())
        currentDate.setMinutes(now.getMinutes())
        currentDate.setSeconds(now.getSeconds())
        currentDate.setMilliseconds(now.getMilliseconds())

        const indonesiaTime = new Date(
          currentDate.getTime() + 7 * 60 * 60 * 1000
        )

        lastReStock = indonesiaTime.toISOString()
      }

      formData.append('lastReStock', lastReStock)

      console.log('Item data yang akan disimpan:', formData)

      const response = await axios.put(
        `http://localhost:8080/api/items/${this.itemId}`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        }
      )

      console.log('Item yang berhasil diupdate:', response.data)

      alert('Item berhasil diupdate')
      this.router.navigate(['/item'])
    } catch (error) {
      console.error('Error updating item:', error)
      alert('Item gagal diupdate')
    }
  }
}
