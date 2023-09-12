import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  ngOnInit(): void {
    if(localStorage.getItem('jwt') != null || localStorage.getItem('oauth2') != null) {
      console.log('User is logged in with : ' + localStorage.getItem('loginMethod'))
    } else {
      console.log('Guest mode')
    }
  }
}
