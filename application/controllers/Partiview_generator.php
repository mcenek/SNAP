<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Partiview_generator extends CI_Controller{
	public $data;
	public $file_dir;
	//public $post;

	public function __construct()
	{
		parent::__construct();
		$this->load->helper(array('form'));
		$this->load->library('form_validation');

		if($this->session->userdata('logged_in')){
			$this->data = $this->session->userdata;
			$this->file_dir = $this->data['file_dir'];
		}
		else{
			redirect('home', 'refresh');
		}
	}

	public function index()
	{
		if($this->session->userdata('logged_in'))
		{
			$files = array_filter(scandir($this->file_dir . '/partiview_generator'), 
		    function($item)
			{
				return !is_dir($this->file_dir.'/' . $item);
			});
			$error = '';
			$user_info = array('files' => $files, 'error' => $error);
			$this->load->view('partiview_generator', $user_info);
		}
	}

	public function partiGeneration()
	{
		$this->index();
		$post=$this->input->post();
		$partiview_path='/Applications/MAMP/htdocs/SNAP/assets/partiViewGen/PartiGen.jar ';
		$output='';
		$cmd='';
		$gexf_file='';
		$file_dates='';
		
		$files=scandir($this->file_dir.'/partiview_generator/');
		foreach ($files as $file) //Set gexf and timestamp files for partiview
		{
			$file_parts=pathinfo($file);
			if($file=="completeLayout.gexf")
			{
				$gexf_file=$this->file_dir.'/partiview_generator/' . $file . ' ';
			}
			if($file=="FileDates.txt")
			{
				$file_dates=$this->file_dir.'/partiview_generator/' . $file ;
			}
			else;
		}
		//-------------------Generate .dl files for every file in preprocessed directory----------------------------------//
		$date_range= $this->session->userdata('date_range');
		$skew_x= $this->session->userdata('skew_x');
		$skew_y= $this->session->userdata('skew_y');
		$skew_z= $this->session->userdata('skew_x');
		$shape= $this->session->userdata('shape');
		$cmd='java'. ' -jar '. $partiview_path. $gexf_file  . $file_dates . " ". $date_range . " ". $skew_x . " ". $skew_y . " ". $skew_z . " " . $shape ;
		
		// if($date_range!="NULL"){
		// 	$cmd='java'. ' -jar '. $partiview_path. $gexf_file  . $file_dates . " ". $date_range . " ". $skew_x . " ". $skew_y . " ". $skew_z . " " . $shape ;
		// }
		// else{
		// 	$cmd='java'. ' -jar '. $partiview_path. $gexf_file  . $file_dates . " 90";

		// }
		
		
		$output=shell_exec($cmd);
		if($output=='')
		{
			$output="Network Visualization Generation failed";
		}
		$this->session->set_flashdata('flash_message', 'Saved to Partiview');
		redirect('partiview_generator', 'refresh');
	}

	public function display_file()
	{
		$file = $this->uri->segment(3);
		$file_path = $this->file_dir . "/partiview_generator/" . $file;
		
		echo nl2br(file_get_contents($file_path));
		exit;
	}






	public function submit_files()//For executing commands
	{
    	if(is_null($this->input->post('checkbox')))
	    {
	        redirect('partiview_generator', 'refresh');//--reload the page
	    }
	    else
	    {
    		if($this->input->post('file_action') == "delete")
    		{
    			$this->delete_files($this->input->post('checkbox'));
    		}
    		else if($this->input->post('file_action') == "download")
    		{
    			$this->download($this->input->post('checkbox'));
    		}
    		else if($this->input->post('file_action') == "kill"){
    			$cmd = "pkill java";
    			shell_exec($cmd);
    			redirect('partiview_generator', 'refresh');
    		}
    		else
    		{
    			$this->partiGeneration($this->input->post('checkbox'));
    		}
	    }
	}

	public function download($files)
	{
		    if(count($files) == 1)
	    {
	        foreach($files as $file => $file_name)
	        {
	            $file_path=$this->file_dir.'/partiview_generator/'.$file_name;
	            if (file_exists($file_path))
	            {
	                header('Content-Description: File Transfer');
	                header('Content-Type: application/octet-stream');
	                header('Content-Disposition: attachment; filename="'.basename($file_path).'"');
	                header('Expires: 0');
	                header('Cache-Control: must-revalidate');
	                header('Pragma: public');
	                header('Content-Length: ' . filesize($file_path));
	                
	                readfile($file_path);
	                // exit;
	            }
	            //exit;
	        }
	    }
	    else
	    {
	        $this->load->library('zip');
	        foreach($files as $file => $file_name)
	        {
	            $file_path=$this->file_dir.'/partiview_generator/'.$file_name;
	            if (file_exists($file_path))
	            {
	                $this->zip->read_file($file_path);
	            }
	        }
	        $this->zip->download('files.zip');
	    }
	    $this->index();
	}

	public function delete_files($files_to_delete){
		$source=$this->file_dir. '/partiview_generator/';
		foreach($files_to_delete as $file){
			$delete[] = $source.$file;
		}
		foreach($delete as $file){
			unlink($file);
		}
		redirect('partiview_generator', 'refresh');
	}
}

/* End of file preprocessed_uploads.php */
/* Location: ./application/controllers/preprocessed_uploads.php */
