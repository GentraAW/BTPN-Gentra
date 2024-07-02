import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { HomeComponent } from './pages/home/home.component'
import { DetailCustomerComponent } from './pages/customer/detail-customer/detail-customer.component'
import { AddCustomersComponent } from './pages/customer/add-customers/add-customers.component'
import { EditCustomerComponent } from './pages/customer/edit-customer/edit-customer.component'
import { AddOrderComponent } from './pages/order/add-order/add-order.component'
import { DetailOrderComponent } from './pages/order/detail-order/detail-order.component'
import { PagesOrderComponent } from './pages/order/pages-order/pages-order.component'
import { AddItemComponent } from './pages/item/add-item/add-item.component'
import { EditItemComponent } from './pages/item/edit-item/edit-item.component'
import { DetailItemComponent } from './pages/item/detail-item/detail-item.component'
import { PagesItemComponent } from './pages/item/pages-item/pages-item.component'
import { NavbarComponent } from './component/navbar/navbar.component'
import { EditOrdersComponent } from './pages/order/edit-orders/edit-orders.component'

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'detail-customer/:customerId', component: DetailCustomerComponent },
  { path: 'add-customer', component: AddCustomersComponent },
  { path: 'edit-customer/:customerId', component: EditCustomerComponent },
  { path: 'add-order', component: AddOrderComponent },
  { path: 'detail-order/:orderId', component: DetailOrderComponent },
  { path: 'order', component: PagesOrderComponent },
  { path: 'add-item', component: AddItemComponent },
  { path: 'edit-item/:itemId', component: EditItemComponent },
  { path: 'detail-item/:itemId', component: DetailItemComponent },
  { path: 'item', component: PagesItemComponent },
  { path: 'navbar', component: NavbarComponent },
  { path: 'edit-order/:orderId', component: EditOrdersComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
