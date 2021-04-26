import React from "react";
import {Button, Col, Form, FormGroup, FormLabel, Image, ListGroup, Modal, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus, faTrash} from "@fortawesome/free-solid-svg-icons";
import AddIngredient from "../Ingredient/AddIngredient";
import Ingredient from "../Ingredient/Ingredient";

class RecipeForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.id,
            title: props.title,
            imageField: props.imageLink != null ? props.imageLink : "",
            imageLink: props.imageLink,
            imageUrlValid: null,
            method: props.method,
            ingredients: props.ingredients
        }
    }

    formatMethod(method) {
        return method.replaceAll(';', '\n');
    }

    handleRecipeMethodChange = (e) => {
        let newValue = e.target.value;
        if (newValue.contains('\n')) {
            newValue.replace('\n', ';')
        }
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value})
    }

    toggleImage = () => {
        const image = this.state.imageLink;
        if (image == null) {
            const imageField = this.state.imageField;
            if (this.isUrlValid(imageField)) {
                this.setState({
                    imageLink: imageField,
                    isUrlValid: true
                });
            } else {
                this.setState({isUrlValid: false});
            }
        } else {
            this.setState({
                imageLink: null,
                imageField: "",
                imageUrlValid: null
            });
        }
    }

    handleCancel = () => {
        this.props.handleCancel(false);
    }

    handleSubmit = () => {
        const methodFormatted = this.state.method.replaceAll('\n', ';');
        const ingredients = [...this.state.ingredients].map(i => ({
            ingredientId: i.id,
            measurementId: i.measurementId,
            quantity: i.quantity
        }))
        this.props.handleSubmit({
            id: this.state.id,
            title: this.state.title,
            imageLink: this.state.imageLink,
            method: methodFormatted,
            ingredients: ingredients
        })
    }

    handleAddIngredient = (ingredient) => {
        let ingredients = [...this.state.ingredients];
        ingredients.push(ingredient)
        this.setState({ingredients: ingredients})
    }

    handleRemoveIngredient = (id) => {
        this.setState(prevState => {
            const ingredients = prevState.ingredients.filter(i => i.id !== id)
            return {ingredients: ingredients}
        });
    }

    handleUpdateIngredient = (ingredientData) => {
        const newQuantity = ingredientData.quantity;
        const ingredientId = ingredientData.id;
        const newMeasurementId = ingredientData.measurementId;
        this.setState(prevState => {
            const ingredients = prevState.ingredients;
            let ingredient = ingredients.find(i => i.id === ingredientId);
            ingredient.quantity = newQuantity;
            ingredient.measurementId = newMeasurementId;
            const index = ingredients.findIndex(i => i.id === ingredientId);
            ingredients.splice(index, 1, ingredient);
            return {ingredients: ingredients};
        })
    }

    isUrlValid = (url) => {
        // https://stackoverflow.com/questions/5717093/check-if-a-javascript-string-is-a-url
        const pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
            '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
            '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
            '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
            '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
            '(\\#[-a-z\\d_]*)?$', 'i'); // fragment locator
        return !!pattern.test(url);
    }

    render() {
        let isImagePresent = this.state.imageLink !== null && this.state.isUrlValid;
        let imageButtonIcon = isImagePresent ? faTrash : faPlus;
        let imageButtonVariant = isImagePresent ? "danger" : "success";
        let notValidWarning = this.state.isUrlValid === false ? "The URL entered is not valid" : "";

        let ingredients = this.state.ingredients;

        return (
            <Form>

                <Modal show={this.props.show} onHide={() => this.props.handleCancel(false)} backdrop="static" size="lg">
                    <Modal.Header>
                        <FormGroup>

                            <FormLabel><h2>Recipe title</h2></FormLabel>

                            <Form.Control value={this.state.title} name="title"
                                          onChange={this.handleChange}/>
                        </FormGroup>
                    </Modal.Header>
                    <Modal.Body>

                        <FormGroup>
                            <FormLabel><h2>Image</h2></FormLabel>
                            {isImagePresent && <Image src={this.state.imageLink} fluid/>}
                            <Form.Text>URL of the image of a recipe. If you are uploading your own picture, please
                                upload your
                                picture to an image hosting platform, like imgur, Google Photos, etc. and paste the link
                                in here</Form.Text>
                            <Row>
                                <Col>
                                    <Form.Control name="imageField" value={this.state.imageField}
                                                  onChange={this.handleChange}
                                                  readOnly={isImagePresent}/>
                                </Col>
                                <Col>
                                    <Button onClick={this.toggleImage} variant={imageButtonVariant}>
                                        <FontAwesomeIcon icon={imageButtonIcon}/>
                                    </Button>
                                </Col>
                            </Row>

                            <Form.Text className="redText">{notValidWarning}</Form.Text>

                        </FormGroup>
                        <FormGroup>
                            <FormLabel><h2>Ingredients</h2></FormLabel>
                            <ListGroup>
                                {ingredients.map((item) =>
                                    <Ingredient {...item} key={item.id}
                                                removeIngredientHandler={this.handleRemoveIngredient}
                                                updateIngredientHandler={this.handleUpdateIngredient}/>
                                )}
                            </ListGroup>

                            <AddIngredient addIngredientHandler={this.handleAddIngredient}
                                           showAlert={this.props.showAlert}
                            />
                        </FormGroup>
                        <FormGroup>
                            <FormLabel><h2>Method</h2></FormLabel>
                            <Form.Control as="textarea" value={this.formatMethod(this.state.method)}
                                          name="method" onChange={this.handleChange}
                            />
                        </FormGroup>

                        <FormGroup>
                            <Button onClick={this.handleSubmit}>Submit</Button>
                            <Button variant="secondary"
                                    onClick={this.handleCancel}>Cancel</Button>
                        </FormGroup>

                    </Modal.Body>
                </Modal>


            </Form>
        );
    }
}

export default RecipeForm;