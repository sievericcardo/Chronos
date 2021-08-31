<template>
    <modal name="login-box" transition="pop-out" :adaptive="true" :width="modalWidth" :height="560">
        <div class="box">
            <div class="box-part" id="bp-left">
                <div class="partition" id="partition-register">
                    <div class="partition-title">{{title}}</div>
                    <div class="partition-form">
                        <form autocomplete="false" v-on:submit.prevent>
                            <div class="field">
                                <label class="label">Username</label>
                                <div class="control has-icons-left has-icons-right">
                                    <input type="text" placeholder="e.g. XX_Strudelbimbo95_XX" class="input"
                                        v-model.trim="username" />
                                    <span class="icon is-small is-left">
                                        <i class="fa fa-user fa-w-14"></i>
                                    </span>
                                    <span class="icon is-small is-right" v-if="$v.username.invalid">
                                        <i class="fas fa-exclamation-triangle"></i>
                                    </span>
                                    <span class="icon is-small is-right"
                                        v-if="$v.username.required && $v.username.minLength">
                                        <i class="fas fa-check"></i>
                                    </span>
                                </div>
                                <p class="help is-success" v-if="$v.username.required && $v.username.minLength">
                                    This username is valid
                                </p>
                                <p class="help is-danger" v-if="!$v.username.required">
                                    Username field cannot be empty
                                </p>
                                <p class="help is-danger" v-if="!$v.username.minLength">
                                    Username field must be at least 4 character long
                                </p>
                            </div>
                            <div class="field" v-if="status">
                                <label class="label">Email</label>
                                <div class="control has-icons-left has-icons-right">
                                    <input type="email" placeholder="e.g. bobsmith@gmail.com" class="input"
                                        v-model.trim="email" required />
                                    <span class="icon is-small is-left">
                                        <i class="fa fa-envelope"></i>
                                    </span>
                                    <span class="icon is-small is-right" v-if="$v.email.invalid">
                                        <i class="fas fa-exclamation-triangle"></i>
                                    </span>
                                    <span class="icon is-small is-right"
                                        v-if="$v.email.required && $v.email.minLength && $v.email.email">
                                        <i class="fas fa-check"></i>
                                    </span>
                                </div>
                                <p class="help is-success"
                                    v-if="$v.email.required && $v.email.minLength && $v.email.email">
                                    This email address is valid
                                </p>
                                <p class="help is-danger" v-if="!$v.email.required">
                                    Email field cannot be empty
                                </p>
                                <p class="help is-danger" v-if="!$v.email.minLength">
                                    Email field must be at least 7 character long
                                </p>
                            </div>
                            <div class="field">
                                <label class="label">Password</label>
                                <div class="control has-icons-left has-icons-right">
                                    <input type="password" placeholder="*******" class="input" v-model.trim="password"
                                        required />
                                    <span class="icon is-small is-left">
                                        <i class="fa fa-lock"></i>
                                    </span>
                                    <span class="icon is-small is-right">
                                        <i class="fas fa-exclamation-triangle" v-if="$v.password.invalid"></i>
                                    </span>
                                    <span class="icon is-small is-right"
                                        v-if="$v.password.required && $v.password.minLength">
                                        <i class="fas fa-check"></i>
                                    </span>
                                </div>
                                <p class="help is-success"
                                    v-if="$v.password.required && $v.password.minLength && $v.password.mustBeCool">
                                    Password valid
                                </p>
                                <p class="help is-danger" v-if="!$v.password.mustBeCool && status">
                                    Password field should contain at least one digit, one lowercase and one uppercase
                                </p>
                                <p class="help is-danger" v-if="!$v.password.required">
                                    Password field cannot be empty
                                </p>
                                <p class="help is-danger" v-if="!$v.password.minLength && status">
                                    Password field must be at least 7 character long
                                </p>
                            </div>
                            <div class="field" v-if="status">
                                <div class="file is-small has-name">
                                    <label class="file-label">
                                        <input class="file-input" type="file" name="resume" @change="fileHandler()"/>
                                        <span class="file-cta">
                                            <span class="file-icon">
                                                <i class="fas fa-upload"></i>
                                            </span>
                                            <span class="file-label">
                                                Avatar
                                            </span>
                                        </span>
                                        <span class="file-name">
                                            {{this.filename}}
                                        </span>
                                    </label>
                                </div>
                                <p class="help is-danger" v-if="file == 0">
                                    File field cannot be empty
                                </p>
                                <p class="help is-success" v-if="file == 1">File valid</p>
                                <p class="help is-danger" v-if="file == 2">
                                    File size must be between 100KB and 800KB and the file must be jpg or png extension
                                </p>
                            </div>
                            <div class="field">
                                <a v-on:click="alreadyRegistered()">
                                    <h1 class="text" ref="alreadylink">
                                        Already registered ? Sign in
                                    </h1>
                                </a>
                            </div>
                            <div class="field">
                                <button ref="loginbutton" class="button is-success" @click="submit()">
                          Sign up
                          </button> 
                            </div>
                        </form>

                        <GoogleLogin class="button is-primary" :params="params" :onSuccess="onSuccess" :onFailure="onFailure" v-if="google == 1">
                            <span class="icon">
                                <img width="24px" style="margin-bottom:3px; margin-right:5px" alt="Google sign-in" src="@/assets/img/glogo.png" />
                            </span>
                            <span>Sign in with Google</span>
                        </GoogleLogin>
                    </div>
                </div>
            </div>
            <div class="box-part" id="bp-right">
                <div class="box-messages"></div>
            </div>
        </div>
    </modal>
</template>

<script>
    import { required, minLength, email } from "vuelidate/lib/validators";
    import CryptoJS from "crypto-js"
    import axios from "axios";
    import GoogleLogin from 'vue-google-login';

    const MODAL_WIDTH = 656;
    const mustBeCool = value => value.match(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/) != null;

    export default {
        components: {
            GoogleLogin
        },
        data() {
            return {
                modalWidth: MODAL_WIDTH,
                status: true,
                username: "",
                password: "",
                email: "",
                file: 0,
                google: 0,
                filename: "",
                filecontent: null,
                params: {client_id: "212356030982-vv8jp0hcm893mbr17tu1ib8epg194gd0.apps.googleusercontent.com"},
                renderParams: {
                    width: 250,
                    height: 40,
                    longtitle: true
                },
                title: "CREATE ACCOUNT"
            };
        },
        created() {
            this.modalWidth = window.innerWidth < MODAL_WIDTH ? MODAL_WIDTH / 2 : MODAL_WIDTH;
        },
        validations: {
            email: {
                required,
                minLength: minLength(7),
                email
            },
            username: {
                required,
                minLength: minLength(4)
            },
            password: {
                required,
                minLength: minLength(8),
                mustBeCool
            }
        },
        methods: {
            submit() {
                if (!this.status) {
                    if (!this.$v.username.invalid && !this.$v.password.invalid) {
                        var user = {
                            name: this.username,
                            image_URL: null,
                            email: null,
                            jwt: null,
                            password: CryptoJS.SHA256(this.password).toString().substring(0, 16)
                        }
                        this.getJwtLogin().then(data => {
                            
                            if(data.message == "Access Denied"){                                
                                this.$awn.alert(data.message)
                            }else{
                                user.jwt = data.jwt;
                                user.image_URL = "https://xelinion.servebeer.com/kp/" + data.avatar.substring(1, data.avatar.length);
                                localStorage.setItem('user', JSON.stringify(user));
                                this.$modal.hide('login-box');
                                this.$awn.success('Welcome back !') 
                            }
                                      
                        });
                        
                    }
                } else {
                    //allora devo controllare la registrazione
                    if (!this.$v.username.invalid &&
                        !this.$v.password.invalid &&
                        !this.$v.email.invalid &&
                        this.file == 1
                    ) {
                        var formData = new FormData();
                        formData.append("username", this.username);
                        formData.append("email", this.email);
                        formData.append("password", this.password);

                        //Genero le mie chiavi
                        formData.append("security_parameters", "random_string");
                        formData.append("file", this.filecontent);

                        axios
                            .post("https://xelinion.servebeer.com/kp/newuser", formData, { 
                                headers: {
                                    "Content-Type": "multipart/form-data"                             
                                }
                            })
                            .then(function (response) {
                                if(response.data.message == "Success"){
                                    this.$modal.hide('login-box');
                                    this.$awn.success('Welcome to Chronos !')
                                }else{
                                    this.$awn.alert(response.data.message)
                                }
                            })
                    }
                }
            },
            alreadyRegistered() {
                if (this.status) {
                    this.$refs.alreadylink.innerText = "Do you need an account ? Sign up";
                    this.$refs.loginbutton.innerText = "Sign in";
                    this.google = 1;
                    this.title= "LOGIN";
                    
                } else {
                    this.$refs.alreadylink.innerText = "Already registered ? Sign in";
                    this.$refs.loginbutton.innerText = "Sign up";
                    this.google = 0;
                    this.title= "CREATE ACCOUNT";
                }
                this.status = !this.status;
            },
            fileHandler() {
                var fileInput = document.querySelector("input[type=file]");
                console.log(fileInput.files[0])
                if (fileInput.files.length > 0) {
                    const fileName = document.querySelector(".file-name");
                    fileName.textContent = fileInput.files[0].name;
                    let ext = fileInput.files[0].name.split(".").pop();
                    let check =
                        fileInput.files[0].size < 800000 &&
                        fileInput.files[0].size > 100000 &&
                        (ext == "png" || ext == "jpg");
                    if (check) {
                        this.file = 1;
                        this.filecontent = fileInput.files[0];
                    } else {
                        this.file = 2;
                    }
                }
            },
            onSuccess(googleUser) {
                //si recuperano alcune informazioni da Google
                var profile = googleUser.getBasicProfile();
                var user = {
                    name: profile.getName(),
                    image_URL: profile.getImageUrl(),
                    email: profile.getEmail(),
                    jwt: null,
                    password: CryptoJS.SHA256(googleUser.getAuthResponse().id_token).toString().substring(0, 16)
                }
                var id_token = googleUser.getAuthResponse().id_token;
                
               //salviamo tutto nel local storage
                this.getJwt(id_token).then(data => {
                    console.log(data);
                    user.jwt = data;
                    localStorage.setItem('user', JSON.stringify(user));
                })

            },
            getJwt(id_token){
                const t = this;
                return axios
                  .post("https://xelinion.servebeer.com/kp/authorize", "", {
                    headers: {
                        'Accept': 'application/json',
                        "Authorization": "Google " + id_token,
                        "Content-Type": "multipart/form-data"
                    }
                  }).then(response => {
                      return response.data.jwt;
                  }).catch(function (error) {
                      t.$awn.error(error)
                  });
            },
            onFailure() {
                console.log("ERRORE")
            },
            getJwtLogin(){
                const t = this;
                return axios
                .post("https://xelinion.servebeer.com/kp/authorize", {
                    username: this.username,
                    password: this.password
                })
                .then(function (response) {

                    return response.data;
                    
                })
                .catch(function (error) {
                    t.$awn.error(error)
                });
            }
        },
    };
</script>

<style scoped>
    @import '~vue-awesome-notifications/dist/styles/style.css';

    GoogleLogin{
        background: white;
        padding: 0;
    }


    .box {
        background: white;
        overflow: hidden;
        width: 656px;
        height: 560px;
        border-radius: 2px;
        box-sizing: border-box;
        box-shadow: 0 0 40px black;
        color: #8b8c8d;
        font-size: 0;
        padding: 0;
    }

    .text {
        font-size: 1rem;
    }

    .box .box-part {
        display: inline-block;
        position: relative;
        vertical-align: top;
        box-sizing: border-box;
        height: 100%;
        width: 50%;
        justify-content: center;
    }

    .box .box-part#bp-right {
        background: url("../assets/img/girl.jpeg") no-repeat top left;
        background-size: cover;
        border-left: 1px solid #eee;
    }

    .box .box-messages {
        position: absolute;
        left: 0;
        bottom: 0;
        width: 100%;
    }

    .box .box-error-message {
        position: relative;
        overflow: hidden;
        box-sizing: border-box;
        height: 0;
        line-height: 32px;
        padding: 0 12px;
        text-align: center;
        width: 100%;
        font-size: 11px;
        color: white;
        background: #f38181;
    }

    .box .partition {
        width: 100%;
        height: 100%;
    }

    .box .partition .partition-title {
        box-sizing: border-box;
        padding: 1rem;
        width: 100%;
        text-align: center;
        letter-spacing: 1px;
        font-size: 20px;
        font-weight: 300;
    }

    .box .partition .partition-form {
        padding: 0 20px;
        box-sizing: border-box;
    }

    .pop-out-enter-active,
    .pop-out-leave-active {
        transition: all 0.5s;
    }

    .pop-out-enter,
    .pop-out-leave-active {
        opacity: 0;
        transform: translateY(24px);
    }

    #bp-right {
        height: inherit;
        padding: 0;
    }

    #bp-left {
        padding-right: 0.5rem;
        padding-left: 0.5rem;
    }
</style>