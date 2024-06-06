import { NgModule } from '@angular/core'
import { RouterModule, Routes } from '@angular/router'
import { HomeComponent } from './pages/home/home.component'
import { DetailCustomerComponent } from './pages/customer/detail-customer/detail-customer.component'

// import { LoginComponent } from './pages/login/login.component';
// import { RegisterComponent } from './pages/register/register.component';
// import { DaftarMakananComponent } from './pages/daftar-makanan/daftar-makanan.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'detail-customer/:customerId', component: DetailCustomerComponent }
  // { path: '', component: LoginComponent },
  // { path: 'daftar-makanan', component: DaftarMakananComponent },
  // { path: 'register', component: RegisterComponent },
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
