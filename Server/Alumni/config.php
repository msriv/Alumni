<?php
function list_directory($directory, &$storage)
{ 
    $files = array();
    $the_directory = opendir($directory) or die("Error $directory doesn't exist");
    while($file = @readdir($the_directory))
    { 
        if ($file == "." || $file == "..") continue;
        if(is_dir($directory.'/'.$file))
        { 
            list_directory($directory.'/'.$file, $storage);
         }
        else
        { 
            $files[] = $file;
         }
     }
    $storage[$directory] = $files;
    closedir($the_directory);
 }
$storage = array();
$path_to_search = '.';
list_directory($path_to_search, $storage);
echo json_encode($storage,JSON_PRETTY_PRINT);
file_put_contents('./config.json',json_encode($storage,JSON_PRETTY_PRINT));
 ?>