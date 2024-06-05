import { NgModule } from '@angular/core'
import { BrowserModule } from '@angular/platform-browser'
import { ReactiveFormsModule } from '@angular/forms' //ReactiveFormsModule untuk login dan register
import { AppRoutingModule } from './app-routing.module'
import { AppComponent } from './app.component'
import { HomeComponent } from './pages/home/home.component'
// import { LoginComponent } from './pages/login/login.component'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
// import { RegisterComponent } from './pages/register/register.component'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
// import { SidebarComponent } from './cores/components/sidebar/sidebar.component'
// import { NavbarComponent } from './cores/components/navbar/navbar.component'
// import { DaftarMakananComponent } from './pages/daftar-makanan/daftar-makanan.component'
// import { TableComponent } from './cores/components/table/table.component'
import { FormsModule } from '@angular/forms' //FormsModule daftar-makanan
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
    // LoginComponent,
    // RegisterComponent,
    // SidebarComponent,
    // NavbarComponent,
    // DaftarMakananComponent,
    // TableComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    FormsModule
  ],

  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
