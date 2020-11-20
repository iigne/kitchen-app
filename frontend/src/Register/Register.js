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
        let toSave = {
            username: this.state.username,
            email: this.state.email,
            password: this.state.password
        }
        axios.post('/user', toSave, {
            "Content-Type":"application/json"
        })
            .then(res => this.setState({success: true})
            )
            .catch(error => this.setState({success: false}));
    }

    validateForm() {
        //TODO check if password match
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
                <Form onSubmit={this.handleSubmit}>
                    {result}
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
            </div>
            );
    }
}

export default Register;