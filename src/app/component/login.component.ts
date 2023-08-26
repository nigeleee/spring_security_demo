import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { GoogleLoginProvider, SocialAuthService } from '@abacritt/angularx-social-login';




@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  user: any;
  loggedIn: any;

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService, private router: Router, private socialAuthService: SocialAuthService) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;

    }

    const val = this.loginForm.value;

    this.authService.login(val.email, val.password)
      .subscribe(
        () => {
          console.log("User is logged in");
          this.router.navigate(['/products']);

        }
      );
  }

  oauth2Login(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then(user => {
        this.user = user;
        this.loggedIn = true;
        console.log('User logged in:', user);
        this.router.navigate(['/products']); // Redirect to products page
      })
      .catch(error => {
        console.error('Error signing in:', error);
      });
  }

  // login() {
  //   this.authService.login('email', 'password').subscribe((data:any) => {
  //     console.log(data);
  //     // Store token or session information
  //     // Navigate to product page
  //     this.router.navigate(['/product']);
  //   });
  // }


  // oauth2Login() {

  //   // return this.authService.oauth2Login();
  //   this.socialAuthService.authState.subscribe((user) => {
  //     this.user = user;
  //     this.loggedIn = (user != null);
  //     console.log(this.user);
  //   });

  // }



}
