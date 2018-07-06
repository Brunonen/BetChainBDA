
<?php
error_reporting(0);
session_start();

$data = json_decode(file_get_contents('php://input'), true); 	

$Respose;
$Response["is_error"] = 0;
$Response["message"] = "";
$Response["data"] = array();

$method = "";
isset($data['method'])? $method = $data['method'] : $Response['message'] = "Access Denied" ;


$config = require("config.php");



if($method == "createHash"){
	$pw = $data['pwd'];
	$salt = $data['salt'];
	$Response["data"] = hash("sha256", $pw . $salt );
}

if($method == "generateUserSalt"){
	$Response["data"] = generateRandomString();
}

if($method == "getUserSalt"){
	$username = $data['username'];
	
	$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
	
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	} 
	
	$username = mysqli_real_escape_string($conn, $username);

	$sql = "SELECT salt FROM tb_users WHERE username = '$username'";
	$result = $conn->query($sql);
	
	$userSalt = "";
	if ($result->num_rows > 0) {
		while($row = $result->fetch_assoc()) {
			$Response["data"] = $row['salt'];
			
		}
	}

	
}

if($method == "register"){
	$pw = isset($data['pwd'])? $data['pwd'] : null;
	$username = isset($data['username'])? $data['username'] : null;
	$userSalt = isset($data['userSalt'])? $data['userSalt'] : null;
	$address = isset($data['address'])? $data['address'] : null;
	$apiKey = isset($data['apiKey'])? $data['apiKey'] : null;
	
	if($pw != null && $username != null && $userSalt != null && strlen($userSalt) == 8 && $apiKey == $config["apiKey"]){
		
		$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
		
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
				
		$pw = mysqli_real_escape_string($conn, $pw);
		$username = mysqli_real_escape_string($conn, $username);
		$userSalt = mysqli_real_escape_string($conn, $userSalt);
		$address = mysqli_real_escape_string($conn, $address);
		$apiKey = mysqli_real_escape_string($conn, $apiKey);
	
		
		$sql = "SELECT * FROM tb_users WHERE username = '$username'";
		$result = $conn->query($sql);
		
		if ($result->num_rows == 0) {	
			try{
				$conn->autocommit(FALSE);
				$sql = "INSERT INTO tb_users (username, pwd, address, salt, showOnline) 
						VALUES ('$username', '$pw', '$address', '$userSalt', '1')";
						
					
				$result = $conn->query($sql);
							
				if ($result) {
					$Response["message"] = "User Successfully registered!";
					$conn->commit();
					
				}else{
					$Response["is_error"] = 1;
					$Response["message"] = "Error while registring user";
					$conn->rollback();
				}
				

				
			}catch(Exception $e){
				$Response["is_error"] = 1;
				$Response["message"] = "Error while registring user";
				$conn->rollback();
			}
		}else{
			$Response["is_error"] = 1;
			$Response["message"] = "A user with that username already exists.";	
		}
			
	
	}else{
		$Response["is_error"] = 1;
		$Response["message"] = "Invalid Request";
	}
}

if($method == "login"){
	$pw = isset($data['pwd'])? $data['pwd'] : null;
	$username = isset($data['username'])? $data['username'] : null;
	
	if($pw != null && $username != null){
			
		$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
		
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		//$pw = mysqli_real_escape_string($conn, $pw);
		//$username = mysqli_real_escape_string($conn, $username);

		$sql = "SELECT * FROM tb_users WHERE username = '$username' and pwd = '$pw'";
		$result = $conn->query($sql);
		
		
		if ($result->num_rows > 0) {
			$Response["message"] = "Success";
			//$Response["data"] = generateRandomString(40);
			while($row = $result->fetch_assoc()) {
				$_SESSION["userID"] = $row['idUser'];
			}
		}else{
			$Response["is_error"] = 1;
			$Response["message"] = " ";
		}
	}else{
		$Response["is_error"] = 1;
		$Response["message"] = "Invalid username or Password";
	}
	
}

if($method == "logout"){
	unset($_SESSION["userID"]);
}

if($method == "getUserInfo"){
	$username = $data["username"];
	
	if(isset($_SESSION["userID"])){
		$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
		
		$username = mysqli_real_escape_string($conn, $username);
		
		$sql = "SELECT * FROM tb_users WHERE username = '$username'";
		$result = $conn->query($sql);
		
		
		if ($result->num_rows > 0) {
			while($row = $result->fetch_assoc()) {
				$Response["data"] = array("username" => $row["username"], "address" => $row["address"], "showOnline" =>  $row["showOnline"]);
			}
		}else{
			$Response["is_error"] = 1;
			$Response["message"] = "No User found";
		}
	}else{
		$Response["is_error"] = 1;
		$Response["message"] = "access denied!";
	}
}

if($method == "getFriendList"){
	if(isset($_SESSION["userID"])){
		
		$userID = $_SESSION["userID"];
		$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
		
		$sql = "SELECT U.username AS username, U.address AS address FROM tb_users AS U
				JOIN tb_friendlist AS F ON U.idUser = F.idUser_2
				WHERE F.idUser_1 = '$userID'";
		
		$result = $conn->query($sql);
		
		
		if ($result->num_rows > 0) {
			$resultArray = array();
			while($row = $result->fetch_assoc()) {
				$resultArray[$row["username"]] = $row["address"];
			}
			
			array_push($Response["data"], $resultArray);
		}else{
			$Response["is_error"] = 1;
			$Response["message"] = "No Friends";
		}
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "access denied";
	}
}

if($method == "getUserByUsername"){
	if(isset($_SESSION["userID"])){
		$username = isset($data['username'])? $data['username'] : null;
		
		if($username != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			
			$username = mysqli_real_escape_string($conn, $username);
			
			$sql = "SELECT username, address FROM tb_users
					WHERE username = '$username' AND showOnline = '1'";
			
			$result = $conn->query($sql);
			
			
			if ($result->num_rows > 0) {
				$resultArray = array();
				while($row = $result->fetch_assoc()) {
					$resultArray[$row["username"]] = $row["address"];
					//$Response["data"] = array("username" => $row["username"], "address" => $row["address"]);
				}
				
				array_push($Response["data"], $resultArray);
				
			}else{
				$Response["is_error"] = 1;
				$Response["message"] = "No User Found";
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}	
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}	
}

if($method == "getUserByQR"){
	if(isset($_SESSION["userID"])){
		$hash = isset($data['hash'])? $data['hash'] : null;
		
		if($hash != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			
			$hash = mysqli_real_escape_string($conn, $hash);
			
			$sql = "SELECT username, address FROM tb_users
					WHERE address = '$hash'";
			
			$result = $conn->query($sql);
			
			
			if ($result->num_rows > 0) {
				$resultArray = array();
				while($row = $result->fetch_assoc()) {
					$Response["data"] = array("username" => $row["username"], "address" => $row["address"]);
				}
				
			}else{
				$Response["is_error"] = 1;
				$Response["message"] = "No User Found";
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}	
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}	
}

if($method == "addFriend"){
	if(isset($_SESSION["userID"])){
		$userToAdd = isset($data['userToAdd'])? $data['userToAdd'] : null;
		
		if($userToAdd != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			$userID = $_SESSION["userID"];
			
			$userToAdd = mysqli_real_escape_string($conn, $userToAdd);
			
			$sql = "SELECT idUser_1, idUser_2 FROM tb_friendlist WHERE idUser_1 = $userID 
			AND idUser_2 = (SELECT idUser FROM tb_users WHERE username = '$userToAdd')";
			
			$result = $conn->query($sql);
			
			if ($result->num_rows == 0) {
				try{
					$conn->autocommit(FALSE);
					$sql = "INSERT INTO tb_friendlist (idUser_1, idUser_2) VALUES ('$userID', (SELECT idUser FROM tb_users WHERE username = '$userToAdd'))";
					
					$result = $conn->query($sql);
					
					
					if ($result) {
						$Response["message"] = "Friend Successfully added!";
						$conn->commit();
						
					}else{
						$Response["is_error"] = 1;
						$Response["message"] = "Error while adding friend";
						$conn->rollback();
					}
				}catch(Exception $e){
					$Response["is_error"] = 1;
					$Response["message"] = "Error while adding friend";		
					$conn->rollback();
				}
			}else{
				$Response["is_error"] = 1;
				$Response["message"] = "Already friends with that user!";
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}	
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}	
}

if($method == "removeFriend"){
	if(isset($_SESSION["userID"])){
		$userToRemove = isset($data['userToRemove'])? $data['userToRemove'] : null;
		
		if($userToRemove != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			$userID = $_SESSION["userID"];
			
			$userToRemove = mysqli_real_escape_string($conn, $userToRemove);
			
			$sql = "SELECT idUser_1, idUser_2 FROM tb_friendlist WHERE idUser_1 = $userID 
			AND idUser_2 = (SELECT idUser FROM tb_users WHERE username = '$userToRemove')";
			
			$result = $conn->query($sql);
			
			if ($result->num_rows == 1) {
				try{
					$conn->autocommit(FALSE);
			
					$sql = "DELETE FROM tb_friendlist WHERE idUser_1 = $userID AND idUser_2 = (SELECT idUser FROM tb_users WHERE username = '$userToRemove')";
					
					$result = $conn->query($sql);
					
					
					if ($result) {
						$Response["message"] = "Friend Successfully removed!";
						$conn->commit();
						
					}else{
						$Response["is_error"] = 1;
						$Response["message"] = "Error while removing Friend";
						$conn->rollback();
					}
				}catch(Exception $e){
					$Response["is_error"] = 1;
					$Response["message"] = "Error while removing Friend";
					$conn->rollback();
				}
			}else{
				$Response["is_error"] = 1;
				$Response["message"] = "You are not Friends with that user anyways!";
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}	
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}	
}

if($method == "createBet"){
	if(isset($_SESSION["userID"])){
		$betTitle = isset($data['betTitle'])? $data['betTitle'] : null;
		$transactionHash = isset($data['transactionHash'])? $data['transactionHash'] : null;
		$participants = isset($data['participants'])? $data['participants'] : null;
		
		if($betTitle != null && $transactionHash != null && $participants != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			$userID = $_SESSION["userID"];
			
			$betTitle = mysqli_real_escape_string($conn, $betTitle);
			$transactionHash = mysqli_real_escape_string($conn, $transactionHash);
		
			$date = date('YYYY-MM-DD', time());
			
			try{	
				$conn->autocommit(FALSE);
			
				$sql = "INSERT INTO tb_bets (title, transactionHash, timestamp) 
						VALUES ('$betTitle', '$transactionHash', '$date')";
						
				$result = $conn->query($sql);
				
				if ($result) {
					$bet_id = $conn->insert_id;
					foreach($participants as $part){
						
						
						$sql = "INSERT INTO tb_bets_x_users (id_Bet, id_User) 
						VALUES ('$bet_id', (SELECT idUser FROM tb_users WHERE address = '$part'))";
						$result = $conn->query($sql);
					}
					
					$Response["message"] = "Bet successfully created!";
					$Response["data"] = $bet_id;
					$conn->commit();
				}else{
					$Response["is_error"] = 1;
					$Response["message"] = "Error while Creating bet";
					$conn->rollback();
				}
			}catch(Exception $e){
				$Response["is_error"] = 1;
				$Response["message"] = "Error while Creating bet";	
				$conn-rollback();
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}
		
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}
}

if($method == "updateBetContractAddress"){
	if(isset($_SESSION["userID"])){
		
		$betID = isset($data['betID'])? $data['betID'] : null;
		$contractAddress = isset($data['contractAddress'])? $data['contractAddress'] : null;
		
		if($contractAddress != null && $betID != null){
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			$userID = $_SESSION["userID"];
			
			$betID = mysqli_real_escape_string($conn, $betID);
			$contractAddress = mysqli_real_escape_string($conn, $contractAddress);
			
			try{
				$conn->autocommit(FALSE);
				$sql = "UPDATE tb_bets SET contractAddress = '$contractAddress' WHERE idBet = '$betID'";
						
				$result = $conn->query($sql);
				
				if ($result) {
					
					$Response["message"] = "Address successfully updated";
					$conn->commit();
				}else{
					$Response["is_error"] = 1;
					$Response["message"] = "Error while updating Address";
					$conn->rollback();
				}
			}catch(Exception $e){
				$Response["is_error"] = 1;
				$Response["message"] = "Error while updating Address";
				$conn->rollback();
			}
		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}
}

if($method == "getUserBets"){
	if(isset($_SESSION["userID"])){
		
		$userID = $_SESSION["userID"];
		
		$filter = isset($data['filter'])? $data['filter'] : null;
		
		$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
		
		$filter = mysqli_real_escape_string($conn, $filter);
		
		$sql = "SELECT B.idBet AS id, B.title AS title, B.contractAddress AS address, B.transactionHash AS txHash, B.timestamp AS creationDate FROM tb_bets AS B
				JOIN tb_bets_x_users AS BxU ON B.idBet = BxU.id_Bet
				WHERE BxU.id_User = '$userID' ORDER BY B.idBet DESC";
		
		$result = $conn->query($sql);
		
		
		if ($result->num_rows > 0) {
			$resultArray = array();
			$betCounter = 0;
			while($row = $result->fetch_assoc()) {
				array_push($resultArray, array("id" => $row["id"], "title" => $row["title"], "contractAddress" => $row["address"], "txHash" => $row["txHash"]));
			}
			
			$Response["data"] = $resultArray;
		}else{
			$Response["is_error"] = 1;
			$Response["message"] = "No Bets";
		}
	}
	else{		
		$Response["is_error"] = 1;
		$Response["message"] = "access denied";
	}
}


if($method == "changeSetting"){
	if(isset($_SESSION["userID"])){
		$settingName = isset($data['settingName'])? $data['settingName'] : null;
		$value = isset($data['value'])? $data['value'] : null;
		
		
		if($settingName != null && $value != null && $settingName != "idUser" && $settingName != "username" && $settingName != "pwd" && $settingName != "salt"){ 
			$conn = new mysqli($config["dbservername"], $config["dbusername"], $config["dbpassword"], $config["dbname"]);
			
			$settingName = mysqli_real_escape_string($conn, $settingName);
			$value = mysqli_real_escape_string($conn, $value);
			
			$userID = $_SESSION["userID"];
			
			try{
				$conn->autocommit(FALSE);
				$sql = "UPDATE tb_users SET $settingName = '$value' WHERE idUser = '$userID'";
				
				$result = $conn->query($sql);
				

				if ($result) {
					$Response["message"] = "Setting Successfully changed!";
					$conn->commit();
					
				}else{
					$Response["is_error"] = 1;
					$Response["message"] = "Error while changing Setting";
					$conn->rollback();
				}
			}catch(Exception $e){
				$Response["is_error"] = 1;
				$Response["message"] = "Error while changing Setting";
				$conn->rollback();
			}

		}
		else{
			$Response["is_error"] = 1;
			$Response["message"] = "Invalid Request";
		}	
	}
	else{
		$Response["is_error"] = 1;
		$Response["message"] = "Access denied";
	}	
}

echo json_encode($Response);

function generateRandomString($length = 8) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

?>
