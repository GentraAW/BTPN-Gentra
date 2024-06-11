import { Component } from '@angular/core'
import {
  faUsers,
  faBox,
  faShoppingCart,
  faFileAlt
} from '@fortawesome/free-solid-svg-icons'

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  faUsers = faUsers
  faBox = faBox
  faShoppingCart = faShoppingCart
  faFileAlt = faFileAlt
}
