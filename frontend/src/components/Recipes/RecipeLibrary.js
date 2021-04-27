import {Button, CardColumns, Container} from "react-bootstrap";
import React from "react";
import RecipeCard from "./RecipeCard";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import BrowseOption from "./BrowseOption";
import RecipeView from "./RecipeView";
import RecipeForm from "./RecipeForm";
import {createRecipe} from "../../api/Api";


class RecipeLibrary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: props.userId,
            currentShow: null,
            recipes: [],
            inRecipeView: false,
            inRecipeViewId: null,
            inCreateRecipe: false
        }
    }

    handleRemoveRecipe = (recipeId) => {
        this.setState(prevState => {
            const recipes = prevState.recipes.filter(r => r.id !== recipeId);
            return {recipes: recipes}
        })
    }

    handleLoadRecipes = (recipes, type) => {
        const sortedRecipes = recipes.sort((a, b) => {
            const aFullDiff = a.ingredients.length - a.ingredients.filter(i => i.recipeQuantity <= i.ownedQuantity).length;
            const bFullDiff = b.ingredients.length - b.ingredients.filter(i => i.recipeQuantity <= i.ownedQuantity).length;
            return aFullDiff - bFullDiff;
        })
        this.setState({recipes: sortedRecipes});
        this.setState({currentShow: type});
    }

    handleUpdateResults = (recipe) => {
        this.setState(prevState => {
            const recipes = prevState.recipes;
            const index = recipes.findIndex(r => r.id === recipe.id);
            recipes.splice(index, 1, recipe);
            return {recipes: recipes}
        })
    }

    handleViewRecipe = (id) => {
        this.setState(prevState => {
            return {
                inRecipeView: !prevState.inRecipeView,
                inRecipeViewId: id
            }
        })
    }

    toggleCreateRecipeMode = (status) => {
        this.setState({inCreateRecipe: status});
    }

    handleSubmitCreatedRecipe = (recipe) => {
        createRecipe(recipe, (res) => {
            this.toggleCreateRecipeMode(false);
            this.props.showAlert("Recipe created", "success");
        }, () => {
            this.props.showAlert("Failed to create recipe", "error");
        })
    }

    render() {
        const recipes = this.state.recipes;
        const type = this.state.currentShow != null ?
            <header className="subheader"> Displaying {this.state.currentShow}</header> : null;
        const noRecipesMessage = this.state.currentShow != null && recipes.length === 0 ?
            <p>No recipes found! Try creating or liking more recipes...</p> : null;
        const inCreateRecipe = this.state.inCreateRecipe;
        return (
            <Container>

                <header className="header">Recipe library</header>
                <header className="subheader">Create</header>

                <Button variant="outline-success" onClick={() => this.toggleCreateRecipeMode(true)}>
                    <FontAwesomeIcon icon={faPlus}/> Create new recipe
                </Button>

                {inCreateRecipe &&

                <RecipeForm id={null} title="" method="" ingredients={[]} image={null}
                            show={this.state.inCreateRecipe}
                            handleCancel={this.toggleCreateRecipeMode}
                            handleSubmit={this.handleSubmitCreatedRecipe}
                            showAlert={this.props.showAlert}
                />
                }

                <header className="subheader">Browse</header>
                <Container>
                    <BrowseOption type="all" text="all recipes" handleLoadRecipes={this.handleLoadRecipes}
                                  showAlert={this.props.showAlert}/>
                    <BrowseOption type="liked" text="recipes I've liked" handleLoadRecipes={this.handleLoadRecipes}
                                  showAlert={this.props.showAlert}/>
                    <BrowseOption type="created" text="recipes I've created" handleLoadRecipes={this.handleLoadRecipes}
                                  showAlert={this.props.showAlert}
                    />
                </Container>

                <Container>
                    {type}
                    {noRecipesMessage}
                    <CardColumns>
                        {recipes.map((item) =>
                            <div key={item.id}>
                                <RecipeCard {...item} userId={this.state.userId}
                                            handleViewRecipe={() => this.handleViewRecipe(item.id)}/>
                                <RecipeView {...item} userId={this.state.userId}
                                            show={this.state.inRecipeView && this.state.inRecipeViewId === item.id}
                                            handleViewRecipe={this.handleViewRecipe}
                                            handleRemoveRecipe={this.handleRemoveRecipe}
                                            handleUpdateResults={this.handleUpdateResults}
                                            showAlert={this.props.showAlert}
                                />
                            </div>
                        )}
                    </CardColumns>
                </Container>

            </Container>
        )
    }


}

export default RecipeLibrary;