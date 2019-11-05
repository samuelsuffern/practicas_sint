<html>

<head>
<meta charset="UTF-8"/>
<title>prueba php</title>
<link rel="stylesheet" href="p1.css">

<script src="p1.js"></script>
</head>

<body id = 'phpPage'>

<h1 id= 'phpTitle'> Ejemplo de página con PHP</h1>

<?php

    $name = $_REQUEST['fname'];
    $peli1 = $_REQUEST['peli1'];
    $peli2 = $_REQUEST['peli2'];
    $peli3 = $_REQUEST['peli3'];
    $apell = $_REQUEST['lname'];
    $navig = $_REQUEST['navegador'];
    $telef = $_REQUEST['telef'];
    $date = $_REQUEST['date'];
    //$name_fich = $_REQUEST['img'];
    $codif = $_REQUEST['codif'];
    $passw = $_REQUEST['pswd'];
    $enviroment1 = getenv('LANG');
    $enviroment2  = getenv('APACHE_LOG_DIR');
    $file = $_FILES['img']['name'];
    $tmp_n = $_FILES['img']['tmp_name'];
    $fichSize = $_FILES['img']['size'];
    $fichType = $_FILES['img']['type'];
    echo "Name:".$name ."<br>";
    echo "Apellidos: " .$apell."<br>";
    echo "Fecha de nacimiento: " .$date."<br>";
    echo "Telefono: " .$telef."<br>";
    echo "Contraseña: ".$passw."<br>";
    echo "Codificacion: ". $codif."<br>";
echo "Navegador del usuario: ". $navig ."<br>";
    echo "Variable de entorno 2: ". $enviroment2."<br>";
echo "hola";
    echo "Variable de entorno 1: ". $enviroment1."<br>";
    echo "Canales de televisión que ves:" . $peli1. " " .$peli2 . " " . $peli3. "<br>";
    //echo "Nombre del fichero: " . $file. " - Ruta temporal: " .$tmp_n.  " - Size: ". $fichSize. " - Type: ". $fichType."<br>";



    $target = '/home/eetlabs.local/sint/sint63/public_html/p1/archivos/'.$file;
    //$targetItem = $file;
    move_uploaded_file($tmp_n, "$target");




    // foreach ($_FILES['img']['error'] as $key => $error) {
    //     if ($error == UPLOAD_ERR_OK) {
    //         $tmp_name = $_FILES['img']['tmp_name'][$key];
    //         // basename() puede evitar ataques de denegación de sistema de ficheros;
    //         // podría ser apropiada más validación/saneamiento del nombre del fichero
    //         $name = basename($_FILES['img']['name'][$key]);

    //     }

    //echo "Moved";
    //}




// print "Nombre = " . $_GET['fname'] . "<br>";
//  print "Contraseña = " . $_GET['pswd'] . "<br>" . PHP_EOL ;

?>
<h3> Imagen subida correctamente: <h3>
<div id=imageD> <img src="archivos/<?php echo $file; ?>"> </div>

</body>
</html>
