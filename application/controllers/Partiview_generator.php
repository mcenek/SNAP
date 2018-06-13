<?php if (!defined('BASEPATH')) exit('No direct script access allowed');

class Partiview_generator extends CI_Controller{
	public $data;
	public $file_dir;
	public $current_project;

	public function __construct()
	{
		parent::__construct();
		$this->load->helper(array('form'));
		$this->load->library('form_validation');

		if($this->session->userdata('logged_in')){

			// $data['projects'] = $this->projects->get_projects($this->session->userdata('id'));
			// $data['current_project'] = $this->projects->get_project($this->session->userdata('project_id'));

			$this->projects->get_project($this->session->userdata('project_id'));
			$this->data = $this->session->userdata;
			$this->file_dir = $this->data['file_dir'];
			$this->current_project = $this->projects->get_project($this->data['project_id']);
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
				return !is_dir($this->file_dir . '/' . $item);
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
		// TODO replace with rel path
		$partiview_path='/Applications/MAMP/htdocs/SNAP/assets/partiViewGen/PartiGen.jar ';
		$output='';
		$cmd='';
		$gexf_file='';
		$file_dates='';

		$files=scandir($this->file_dir . '/partiview_generator/');
		foreach ($files as $file) //Set gexf and timestamp files for partiview
		{
			// TODO one of these isn't needed anymore...
			$file_parts=pathinfo($file);
			if($file=='completeLayout.gexf')
			{
				$gexf_file=$this->file_dir . '/partiview_generator/' . $file . ' ';
			}
			if($file=='FileDates.txt')
			{
				$file_dates=$this->file_dir . '/partiview_generator/' . $file ;
			}
			else;
		}

		$date_range= $this->session->userdata('date_range');
		$skew_x= $this->session->userdata('skew_x');
		$skew_y= $this->session->userdata('skew_y');
		$skew_z= $this->session->userdata('skew_x');
		$shape= $this->session->userdata('shape');
		$cmd='java' . ' -jar ' . $partiview_path . $gexf_file . $file_dates . ' '
			. $date_range . ' ' . $skew_x . ' ' . $skew_y . ' ' . $skew_z . ' ' . $shape ;

		$output=shell_exec($cmd);
		if($output=='')
		{
			$output='Network Visualization Generation failed';
		}
		$this->session->set_flashdata('flash_message', 'Saved to Partiview');
		redirect('partiview_generator', 'refresh');
	}

	public function threejsFileGeneration()
	{
		$this->index();
		$post=$this->input->post();
		$threejsgen_path=$this->config->item('base_directory') . 'assets/ThreeJsGen/ThreeJsGen.jar';
		$output='';
		$cmd='';
		$gexf_dir=$this->file_dir . '/partiview_generator/';

		$project_name=$this->current_project->name;
		$date_range=$this->session->userdata('date_range');
		$skew_x=$this->session->userdata('skew_x');
		$skew_y=$this->session->userdata('skew_y');
		$skew_z=$this->session->userdata('skew_x');
		$shape=$this->session->userdata('shape');
		$cmd=sprintf('java -jar %s %s %s %s %s %s %s %s', $threejsgen_path, $project_name,
			$date_range, $skew_x, $skew_y, $skew_z, $shape, $gexf_dir);

		$output=shell_exec($cmd);
		if($output=='')
		{
			$output='Threejs file generation failed';
		}
		redirect('partiview_generator', 'refresh');
	}

	public function get_colors()
	{
		$curr_proj = $this->projects->get_project($this->session->userdata('project_id'))->name;
		echo file_get_contents($this->file_dir . '/partiview_generator/'
			. $curr_proj
			. '_meta-colors.three.txt');
	}

	public function get_layers()
	{
		$curr_proj = $this->projects->get_project($this->session->userdata('project_id'))->name;
		echo file_get_contents($this->file_dir . '/partiview_generator/'
			. $curr_proj
			. '_layers.three.txt');
	}

	public function get_edges()
	{
		$curr_proj = $this->projects->get_project($this->session->userdata('project_id'))->name;
		echo file_get_contents($this->file_dir . '/partiview_generator/'
			. $curr_proj
			. '_edges.three.txt');
	}

	public function get_noodles()
	{
		$curr_proj = $this->projects->get_project($this->session->userdata('project_id'))->name;
		echo file_get_contents($this->file_dir . '/partiview_generator/'
			. $curr_proj
			. '_noodles.three.txt');
	}

	public function display_file()
	{
		$file = $this->uri->segment(3);
		$file_path = $this->file_dir . '/partiview_generator/' . $file;

		echo nl2br(file_get_contents($file_path));
		exit;
	}

	//For executing commands
	public function submit_files()
	{
		if(is_null($this->input->post('checkbox')))
		{
			redirect('partiview_generator', 'refresh');//--reload the page
		}
		else
		{
			if($this->input->post('file_action') == 'delete')
			{
				$this->delete_files($this->input->post('checkbox'));
			}
			else if($this->input->post('file_action') == 'download')
			{
				$this->download($this->input->post('checkbox'));
			}
			else if($this->input->post('file_action') == 'kill')
			{
				$cmd = 'pkill java';
				shell_exec($cmd);
				redirect('partiview_generator', 'refresh');
			}
			else if($this->input->post('file_action') == 'vis_gen')
			{
				$this->partiGeneration($this->input->post('checkbox'));
				$this->threejsFileGeneration($this->input->post('checkbox'));
			}
		}
	}

	// TODO almost constant download and delete functions like these should be
	// stored in one place, not rewritten every page
	public function download($files)
	{
		if(count($files) == 1)
		{
			foreach($files as $file => $file_name)
			{
				$file_path=$this->file_dir . '/partiview_generator/' . $file_name;
				if (file_exists($file_path))
				{
					header('Content-Description: File Transfer');
					header('Content-Type: application/octet-stream');
					header('Content-Disposition: attachment; filename="' . basename($file_path) . '"');
					header('Expires: 0');
					header('Cache-Control: must-revalidate');
					header('Pragma: public');
					header('Content-Length: ' . filesize($file_path));

					readfile($file_path);
				}
			}
		}
		else
		{
			$this->load->library('zip');
			foreach($files as $file => $file_name)
			{
				$file_path=$this->file_dir . '/partiview_generator/' . $file_name;
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
		$source=$this->file_dir . '/partiview_generator/';
		foreach($files_to_delete as $file){
			$delete[] = $source.$file;
		}
		foreach($delete as $file){
			unlink($file);
		}
		redirect('partiview_generator', 'refresh');
	}
}
