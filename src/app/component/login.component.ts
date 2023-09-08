import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm!: FormGroup;
  user: any;
  loggedIn: any;
  loginMethod!: any;
  sub$!: Subscription;
  emailErrorMsg: string = '';
  passwordErrorMsg: string = '';

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService, private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;

    }

    const val = this.loginForm.value;

    this.authService.login(val.email, val.password)
      .subscribe({
        next : (response) => {
          console.log("User is logged in");
          localStorage.setItem('loginMethod', 'jwt');
          this.router.navigate(['/products']);
          this.emailErrorMsg = '';  // Clear any existing error messages
          this.passwordErrorMsg = '';  // Clear any existing error messages
        },
        error: (err) => {
          if (err.status === 401) {
            const message = err.error.message.toLowerCase();
            if (message.includes('email')) {
              this.emailErrorMsg = 'Invalid email';
            } else if (message.includes('password')) {
              this.passwordErrorMsg = 'Invalid password';
            } else {
              this.emailErrorMsg = 'An unexpected error occurred';
              this.passwordErrorMsg = 'An unexpected error occurred';
            }
          }
          console.log(err);
        }
      }
    );

  }

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }

  initiateOAuth2Login() {
    console.log(">>>>>>>>>>>>>> initiated oauth2 login")
    this.authService.OAuth2Login();
  }

}
