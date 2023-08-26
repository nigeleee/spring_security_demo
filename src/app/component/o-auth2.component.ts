import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-o-auth2',
  templateUrl: './o-auth2.component.html',
  styleUrls: ['./o-auth2.component.css']
})
export class OAuth2Component implements OnInit {
  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const code = params['code']; // or 'token', depending on the OAuth2 response
      // Handle the code or token, send it to backend, etc.
    });
  }
}
