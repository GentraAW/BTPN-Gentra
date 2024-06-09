import { NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import { ReactiveFormsModule } from '@angular/forms'
import { AppRoutingModule } from './app-routing.module'
import { CommonModule } from '@angular/common'
import { MatCardModule } from '@angular/material/card'
import { AppComponent } from './app.component'
import { HomeComponent } from './pages/home/home.component'
import { DetailCustomerComponent } from './pages/customer/detail-customer/detail-customer.component'
import { AddCustomersComponent } from './pages/customer/add-customers/add-customers.component'
import { EditCustomerComponent } from './pages/customer/edit-customer/edit-customer.component'
import { AddOrderComponent } from './pages/order/add-order/add-order.component'
import { EditOrderComponent } from './pages/order/edit-order/edit-order.component'
import { DetailOrderComponent } from './pages/order/detail-order/detail-order.component'
import { PagesOrderComponent } from './pages/order/pages-order/pages-order.component'
import { AddItemComponent } from './pages/item/add-item/add-item.component'
import { EditItemComponent } from './pages/item/edit-item/edit-item.component'
import { DetailItemComponent } from './pages/item/detail-item/detail-item.component'
import { PagesItemComponent } from './pages/item/pages-item/pages-item.component'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { FormsModule } from '@angular/forms'
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    DetailCustomerComponent,
    AddCustomersComponent,
    EditCustomerComponent,
    AddOrderComponent,
    EditOrderComponent,
    DetailOrderComponent,
    PagesOrderComponent,
    AddItemComponent,
    EditItemComponent,
    DetailItemComponent,
    PagesItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatCardModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    FormsModule,
    CommonModule
  ],

  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
