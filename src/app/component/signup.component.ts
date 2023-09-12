import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersService } from '../service/users.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, OnDestroy {
  signupForm!: FormGroup;
  showPassword: boolean = false;
  sub$! : Subscription;
  duplicateEmailMsg!: string;

  constructor(private usersService : UsersService, private router : Router, private fb : FormBuilder) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm() {
    this.signupForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      phone: ['', [Validators.required, Validators.maxLength]],
      address: ['', [Validators.required]],
      password: ['', [Validators.required]],
      // retypePassword: ['', [Validators.required]]
    });
  }

  createUser() {
    this.sub$ = this.usersService.create(this.signupForm.value).subscribe({
      next : (response) => {
        console.log(response);
        alert("Registered Successfully. An email has been sent to your email address. Please validate and log in again.")
        this.router.navigateByUrl('/login');
      },

      error : (err) => {
        this.duplicateEmailMsg = err.error.message;
        console.log(err);
      }
    })
  }

  ngOnDestroy(): void {
    if (this.sub$) {
      this.sub$.unsubscribe();
    }
  }

}

