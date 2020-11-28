import React, {Component} from "react";
import {Button, Form, Alert} from "react-bootstrap";
import axios from "axios";


class Register extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            email: "",
            password: "",
            confirmPassword: "",
            success: null,
            error: ""
        }
        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleChange(e) {
        this.setState({[e.target.name] : e.target.value})
    }

    handleSubmit(e) {
        e.preventDefault()
        if(this.validateForm()) {
            let toSave = {
                username: this.state.username,
                email: this.state.email,
                password: this.state.password
            }
            axios.post('auth/register', toSave, {
                "Content-Type":"application/json"
            })
                .then(res => this.setState({success: true})
                )
                .catch(error => {
                    this.setState({success: false});
                    this.setState({error: error.response.data})
                });
        } else {
            this.setState({success: false});
        }
    }

    validateForm() {
        if(this.state.username.length === 0) {
            this.setState({error: "Username must not be blank"})
            return false;
        }
        if(this.state.email.length === 0) {
            this.setState({error: "Email must not be blank"})
            return false;
        }
        if(this.state.password !== this.state.confirmPassword) {
            this.setState({error: "Passwords must match"})
            return false;
        }
        this.setState({success: null});
        this.setState({error: ""})
        return true;
    }

    render() {
        let result
        if(this.state.success != null) {
            result = this.state.success ?
                <Alert variant={"success"}>Successfully registered! You can now log in.</Alert> :
                <Alert variant={"danger"}>{this.state.error}</Alert>
        }

        return(
            <div className="Register">
                <h2>Register</h2>
                {result}
                {!(this.state.success != null && this.state.success===true) &&
                <Form onSubmit={this.handleSubmit} hidden={this.state.success != null && this.state.success===true}>
                    <Form.Group controlId="formUsername">
                        <Form.Label>Username</Form.Label>
                        <Form.Control name="username" type="text" value={this.state.username} onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control name="email" type="email" value={this.state.email} onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control name="password" type="password" value={this.state.password} onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Confirm password</Form.Label>
                        <Form.Control name="confirmPassword" type="password" value={this.state.confirmPassword} onChange={this.handleChange}/>
                    </Form.Group>

                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
                }
            </div>
            );
    }
}

export default Register;