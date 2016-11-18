<?php

if(isset($_GET['preview'])) {
    $themeName = str_replace('/', '', $_GET['preview']);
    if(file_exists("themes/$themeName/preview.jpg")) {
        header('Content-Type: image/jpeg');
        echo file_get_contents("themes/$themeName/preview.jpg");
        exit();
    }
    http_response_code(404);
    exit();
}

if(isset($_GET['download'])) {
    $themeName = str_replace('/', '', $_GET['download']);
    if(file_exists("themes/$themeName/$themeName.cdstheme")) {
        header('Content-Type: application/zip');
        header('Content-Disposition: attachment; filename="' . $themeName . '.cdstheme"');
        echo file_get_contents("themes/$themeName/$themeName.cdstheme");
        exit();
    }
    http_response_code(404);
    exit();
}

$files = scandir('themes/');
$results = '';
foreach ($files as $file) {
    if($file == '.' || $file == '..') {
        continue;
    }
    $results .= $file . ":0\n";
}

echo trim($results);
