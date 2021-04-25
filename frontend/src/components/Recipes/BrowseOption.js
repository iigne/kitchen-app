import {Button, Col, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faGlobe, faHeart, faLeaf, faPencilAlt} from "@fortawesome/free-solid-svg-icons";
import React from "react";
import authHeader from "../../api/auth-header";
import axios from "axios";


class BrowseOption extends React.Component {
    constructor(props) {
        super(props);
        const option = this.getOption(props.type)
        this.state = {
            text: props.text,
            type: props.type,
            icon: option.icon,
            variant: option.variant
        }
    }

    getOption(type) {
        let icon, variant;
        switch (type) {
            case "all":
                icon = faGlobe;
                variant = "primary";
                break;
            case "suggestion":
                icon = faLeaf;
                variant = "success";
                break;
            case "liked":
                icon = faHeart;
                variant = "danger";
                break;
            case "created":
                icon = faPencilAlt;
                variant = "secondary";
                break;
            default:
                break;
        }
        return {icon: icon, variant: variant}
    }

    handleClick = () => {
        axios.get('/recipe/list/',
            {
                params: {option: this.state.type},
                headers: authHeader()
            })
            .then(res => {
                this.props.handleLoadRecipes(res.data, this.state.text);
            }).catch(err => {
            this.props.showAlert("Failed to fetch recipes", "error");
            console.log(err)
        })
    }

    render() {
        return (
            <Row>
                <Col xs={2}>
                    <Button variant={this.state.variant} onClick={this.handleClick}>
                        <FontAwesomeIcon icon={this.state.icon}/>
                    </Button>
                </Col>
                <Col>
                    Browse {this.state.text}
                </Col>
            </Row>
        )
    }
}

export default BrowseOption;