import {Button, Col, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import React from "react";


class IconButtonLabel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            label: props.label,
            icon: props.icon,
            variant: props.variant
        }
    }

    static getDerivedStateFromProps(props, state) {
        if (props.label !== state.label) {
            return {
                label: props.label,
                icon: props.icon
            };
        }
        return null;
    }

    render() {
        return (
            <Row>
                <Col xs={2}>
                    <Button variant={this.state.variant} onClick={() => this.props.handleClick()}>
                        <FontAwesomeIcon icon={this.state.icon}/>
                    </Button>
                </Col>
                <Col>
                    {this.state.label}
                </Col>
            </Row>
        )
    }
}

export default IconButtonLabel;