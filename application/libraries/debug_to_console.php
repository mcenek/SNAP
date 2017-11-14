<?php
defined('BASEPATH') or exit('No direct script access allowed');

class Debug_To_Console {
	public function debug_out($data){
		if(is_array($data)){
			$output = "<script>console.log('Debug Objects: " . implode(',', $data) . "');</script>";
		}
		else {
			$output = "<script>console.log('Debug Objects: " . $data . "');</scripts>";
		}
		echo $output;
	}
}
?>
