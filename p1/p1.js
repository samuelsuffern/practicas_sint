function peter(){
    alert("hola mundo");
    console.log("hola mundo");

}
function checkAll() {
  document.forms.hola.elements.peli1.checked = true;
  document.forms.hola.elements.peli2.checked = true;
  document.forms.hola.elements.peli3.checked = true;
document.forms.hola.elements.none.checked = false;
  }

  function uncheckAll() {
    document.forms.hola.elements.peli1.checked = false;
    document.forms.hola.elements.peli2.checked = false;
    document.forms.hola.elements.peli3.checked = false;
    document.forms.hola.elements.all.checked = false;
  }
  function getNavigator(){
  document.getElementById("navig").value = window.navigator.userAgent;

  }
function checkPass(){
   var pass = document.getElementById("pass");
   if(pass.value.length == 0){
     return;
   }else if(pass.value.length < 5){
        alert("Password too short");
    }


}

function sendMethod(value){
document.forms.hola.method = value;
//alert(document.forms.hola.sendMethodVar);

}

function sendPage(value){
    document.forms.hola.action = value;
    //alert(document.forms.hola.sendMethodVar);

    }


function sendCodif(value){
    document.forms.hola.enctype = value;
    //alert(document.forms.hola.sendMethodVar);

    }
