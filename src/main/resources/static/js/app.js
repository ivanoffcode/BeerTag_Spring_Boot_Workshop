const search = () => {
    const term = document.getElementById('beer-search-field').value;
    console.log(`You've searched for ${term}`); 
}

const onKeyDown = () => {
    console.log(document.getElementById('beer-search-field').value)
}

const toggleSearchBar = () => {
    const element = document.getElementById('searchBar');

    if(element.style.display === "none") {
        element.style.display = "block";
    } else {
        element.style.display = "none";
    }
}