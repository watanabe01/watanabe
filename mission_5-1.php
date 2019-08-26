<?php
$msg = "";                  //掲示板の変数 
$nameset = "名前";
$commentset = "コメント";
$changenum   = 0;          

$username ='de-tabe-su';     //データベースの変数
$host     ='host';
$passward     ='password';
$database ='de-tabe-su';   
$table_name = 'keiziban';
$table_nakami = "id INT(6) PRIMARY KEY AUTO_INCREMENT, namae VARCHAR(20), bun VARCHAR(50), toki VARCHAR(20), keywaord VARCHAR(20)";
$num = 1;


$link = new mysqli($host,$username,$passward,$database);   //データベースへの接続
/* 
   if (!$link) {                                       //接続有無の確認
        echo ('Connect Error (' . mysqli_connect_errno() . ') '. mysqli_connect_error());//エラー
           }

   echo 'Success... ' . mysqli_get_host_info($link) . "      ".$database."\n<br>";  //接続が完了しているかの確認
  
*/
/*
   $sql = "CREATE TABLE ".$table_name." (id INT(6) PRIMARY KEY AUTO_INCREMENT, namae VARCHAR(20), bun VARCHAR(50), toki VARCHAR(20), keywaord VARCHAR(20));";
   //テーブル作成

   if ($link->query($sql) === TRUE) {
      echo "Table ".$table_name." created successfully";
     } 
   else {
       echo "Error creating table: " . $link->error;
         }
*/
///////////テーブル(all)を表示////////////
/*
 $sql = "SHOW TABLES FROM ".$database;

 $result = $link ->query($sql);

 while($row = mysqli_fetch_row($result)){
    $table_list[] = $row;
    }

 echo "<br><br>table lists:<br>";

 foreach($table_list as $srt){
   foreach($srt as $status){
      echo $status." ";
         } 
    echo "<br>";
       }
*/
///////////テーブルの中身を表示///////////

/*
 $sql = "SHOW COLUMNS FROM ".$table_name;

 $result = $link ->query($sql);


while($row = mysqli_fetch_row($result)){
    $table_data[] = $row;
    }

 echo "<br>table ststus:     ".$table_name."<br>";


 foreach($table_data as $srt){
    foreach($srt as $status)  {   
       
       echo $status." ";
         }
       echo "<br>";
       }
 echo "<br>";

*/



//////////////////////名前とコメントが書かれているとき（入力フォーム)///////////////////////
if (isset($_POST['send']))
{
/////////////////////新規登録モード////////////////////////////////////////////////////////
 if(isset($_POST['comment']) && isset($_POST['name']) && !isset($_POST['changenum']) || $_POST['changenum'] == 0 )
 {
 
   if( !$_POST['comment'] || $_POST['comment'] == "" || !$_POST['name'] || $_POST['name'] == ""){$msg = "※名前もしくはコメントが未入力です。";}
   else if (!$_POST['pass'] || $_POST['pass'] == "" ){$msg = "パスワード未入力です";}
   else 
   {
 
   $comment = $_POST['comment'];
   $name    = $_POST['name'];
   $zikan   = date('Y/m/d G:i:s');
   $pass    = $_POST['pass'];
  


//データベースに登録//
  $connect = mysqli_connect( $host, $username, $passward);

  $sql = "INSERT INTO ".$table_name."(namae,bun,toki,keywaord) VALUES ('".$name."','".$comment."','".$zikan."','".$pass."');"; 
  $result = $link ->query($sql);
 //if($result == 1){echo "insert seikou";}
 else{echo "insert sippai";}

   $msg = $_POST['comment'];
   
   }
  }

////////////////////////編集モード////////////////////////////////////////////////

else if(isset($_POST['comment']) && isset($_POST['name']) && $_POST['changenum'] >= 0)
 {


    if( !$_POST['comment'] || $_POST['comment'] == "" || !$_POST['name'] || $_POST['name'] == ""){$msg = "※名前もしくはコメントが未入力です。";}
  else if (!$_POST['pass'] || $_POST['pass'] == "" ){$msg = "パスワード未入力です";}
   else 
  {
   
     $comment = $_POST['comment'];
     $name    = $_POST['name'];
     $zikan   = date('Y/m/d G:i:s');
     $pass    = $_POST['pass'];
////////changenumの投稿を編集
    
    $sql = "UPDATE ".$table_name." SET namae='".$name."', bun='".$comment."', toki='".$zikan."'  WHERE id=".$_POST['changenum'];
    $result = $link -> query($sql);
   // if($result == 1){echo "hennsyu seikou";}
    
   }
  }

 
 
  $mode = "新規登録モード";
}



//////////////////////削除フォーム////////////////////////////
else if (isset($_POST['sakujo']))
 {


   if (!$_POST['delete'] || $_POST['delete'] == "" || ($_POST['delete'] <= 0)){$msg = "削除対象番号が未入力または無効です！";}
   else {

   ////番号取得（削除）
      $numflag = 0;

      $sql = "SELECT id,keywaord FROM ".$table_name;//入っている番号すべて持ってくる
      $result = $link -> query($sql);
      

        while($row = mysqli_fetch_row($result)){
        $sakujo_hensu[] = $row;
        }
      
       foreach($sakujo_hensu as $srt){
         if($srt[0] == $_POST['delete']){
                                          $numflag = 1;//削除番号発見で１に
                                          $passward = $srt[1];//passward取得
                              //echo $srt[0]."<br>".$srt[1].$_POST['pass'].$passward."<br>".strcmp($_POST['pass'],$passward);
                       }
            }




     if($numflag == 0 ){$msg = "投稿番号が存在しません";}
     else {
          
           if($passward == $_POST['pass'])
           {
//番号の投稿削除
           
             $sql = "DELETE FROM ".$table_name." WHERE id=".$_POST['delete'];
             $result = $link -> query($sql);
           
             $msg = $_POST['delete']."番を削除しました。";
           
           }
           else{$msg = "パスワードが違います";}
         }

     }

    $mode = "新規登録モード";
 }


//////////////////////編集フォーム////////////////////////////
else if (isset($_POST['hensyu']))
 {

    if (!$_POST['change'] || $_POST['change'] == "" || ($_POST['change'] <= 0)){$msg = "編集対象番号が未入力または無効です！";$mode = "新規登録モード";}
   else {

////編集投稿番号を探す
////番号取得（編集）
      $numflag = 0;

      $sql = "SELECT id,keywaord FROM ".$table_name;//入っている番号すべて持ってくる
      $result = $link -> query($sql);
      
      
        while($row = mysqli_fetch_row($result)){
        $hensyu_numpass[] = $row;
        }
      
      
      foreach($hensyu_numpass as $srt){
         if($srt[0] == $_POST['change']){
                                          $numflag = 1;//削除番号発見で１に
                                          $passward = $srt[1];}//passward取得
         

          }

      if($numflag == 0 ){$msg = "投稿番号が存在しません";}
      else {////見つかった時
        if($passward != $_POST['pass']){$msg = "パスワードが違います";}
          else{

    /////編集する投稿の名前とコメント,番号を取得し、次のフォームに表示する
            
            $sql = "SELECT * FROM ".$table_name;//データすべて持ってくる
            $result = $link -> query($sql);
            
             while($row = mysqli_fetch_row($result)){
              $hensyu_all[] = $row;
               }
      
            foreach($hensyu_all as $srt){
               if($srt[0] == $_POST['change']){
                                          $numflag = 1;//編集番号発見で１に
                                          $nameset = $srt[1];             //名前の元データ 
                                          $commentset = $srt[2];          //コメントの元データ
                                          $changenum  = $srt[0];          //編集投稿番号取得
                                          $passward = $srt[4];              //passward取得
                                   }
                }
             $msg = $_POST['change'].$changenum."番を編集します";
            
             $mode = "編集モード";
            }
         
         }  
    }

 }







/////////////////開始フォーム//////////////////////////////

else 
  { 
   //テーブルの中身をすべて削除
  $sql = "TRUNCATE TABLE ".$table_name;
  $result = $link ->query($sql);
  
 // if ($result == 1){echo "sakujo seikou<br>";}

  $mode = "新規登録モード";
  }


?>






<html>
<head> <title> 掲示板</title>
</head>

<body> <form method ="post" action = "mission_5-1.php">
<?php
  if($mode != ""){echo $mode."<br><br>";}
?>
<p> <input type = "text" name = "name" value = <?php echo $nameset; ?>> </p>
<p> <input type = "text" name = "comment" value = <?php echo $commentset; ?>> </p>
<p> パスワード入力： <input type = "text" name = "pass" > </p>

<?php 
  echo $msg."<br>";
?>
<p> <input type = "submit" name = "send" value = "送信"> </p>

<?php  //編集モードなら表示なし

if($mode == "新規登録モード"){
?>
<p>削除番号： <input type = "text" name = "delete">   <input type = "submit" name = "sakujo" value = "削除"> </p>

<p>編集番号： <input type = "text" name = "change">   <input type = "submit" name = "hensyu" value = "編集"> </p>
<?php
}
?>


<p><input type = "hidden" name = "changenum" value = <?php if($changenum != 0){echo $changenum;} ?>>  </p>


<?php
   
   /////////データベースから持ってきて表示
  $sql = "SELECT * FROM ".$table_name;
  $result = $link -> query($sql);

  $table_tus_after[] =['ID','NAME','COMMENT','TIME','PASS'];

 while($row = mysqli_fetch_row($result)){
    $table_tus_after[] = $row;
    }
//  var_dump($table_tus_after);
 

 foreach($table_tus_after as $srt){
      echo $srt[0]."<>".$srt[1]."<>".$srt[2]."<>".$srt[3]."<>".$srt[4];
      echo "<br>";
    }

 echo "<br>";

?>
</body>
<html>