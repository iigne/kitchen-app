import {Button, CardColumns, Container} from "react-bootstrap";
import React from "react";
import RecipeCard from "./RecipeCard";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import BrowseOption from "./BrowseOption";
import RecipeView from "./RecipeView";


class RecipeLibrary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: props.userId,
            currentShow: null,
            recipes: [],
            inRecipeView: false
        }
    }

    handleLoadRecipes = (recipes, type) => {
        this.setState({recipes: recipes})
        this.setState({currentShow: type})
    }

    handleViewRecipe = () => {
        this.setState(prevState => {
            return {inRecipeView: !prevState.inRecipeView}
        })
    }

    render() {
        let recipes = this.state.recipes
        let type = this.state.currentShow != null ?
            <header className="subheader">Displaying {this.state.currentShow}</header> : null;
        return (
            <Container>

                <header className="header">Recipe library</header>
                <header className="subheader">Create</header>

                <Button variant="outline-success">
                    <FontAwesomeIcon icon={faPlus}/> Create new recipe
                </Button>

                <header className="subheader">Browse</header>
                <Container>
                    <BrowseOption type="suggestion" text="recipe suggestions"
                                  handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="all" text="all recipes" handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="liked" text="recipes I've liked" handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="created" text="recipes I've created"
                                  handleLoadRecipes={this.handleLoadRecipes}/>
                </Container>

                <Container>
                    {type}
                    <CardColumns>
                        {recipes.map((item) =>
                            <>
                                <RecipeCard {...item} userId={this.state.userId} key={item.id} handleViewRecipe={this.handleViewRecipe}/>
                                <RecipeView {...item} userId={this.state.userId} show={this.state.inRecipeView} hide={this.handleViewRecipe}/>

                            </>
                        )}
                    </CardColumns>
                </Container>

            </Container>
        )
    }


}

export default RecipeLibrary;