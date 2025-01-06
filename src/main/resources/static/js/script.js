console.log("Script Loader")

let currentTheme = getTheme();
changeTheme();

function changeTheme(){
    // set to webpage
    document.querySelector('html').classList.add(currentTheme);

    // set listner for button
    const changeThemeBtn = document.querySelector('#theme-change-btn')

    //change text
    changeThemeBtn.querySelector('span').textContent = currentTheme == 'light'?"dark":"light";

    changeThemeBtn.addEventListener("click", (event) =>{
        console.log('clicked');
        
        const oldTheme = currentTheme;

        if(currentTheme === "dark"){
            currentTheme = "light";
        }else{
            currentTheme = "dark"
        }
        //update local storage
        setTheme(currentTheme);

        //remove current theme
        document.querySelector("html").classList.remove(oldTheme);

        //set current theme
        document.querySelector("html").classList.add(currentTheme);

        //change text
        changeThemeBtn.querySelector('span').textContent = currentTheme == 'light'?"dark":"light";
        
    })
}

//set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme", theme)
}

//get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme")
    return theme?theme : "light";
}
