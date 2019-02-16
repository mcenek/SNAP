<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Semantic_networks extends CI_Controller
{
    public $data;
    public $file_dir;

    public function __construct()
    {
        parent::__construct();
        $this->load->helper(array('form'));
        $this->load->library('form_validation');

        if ($this->session->userdata('logged_in')) {
            $this->data = $this->session->userdata;
            $email = $this->data['email'];
            $project_name = $this->data['project_name'];
            $this->file_dir = config_item('user_directory') . "{$email}/{$project_name}";
        } else {
            redirect('home', 'refresh');
        }
    }

    public function index()
    {
        if ($this->session->userdata('logged_in')) {
            $files = array_filter(scandir($this->file_dir . '/semantic_networks'),
                function ($item) {
                    return !is_dir($this->file_dir . '/' . $item);
                });
            $error = '';
            $user_info = array('files' => $files, 'error' => $error);
            $this->load->view('semantic_networks', $user_info);
        }
    }

    //Transfer .dl files to Semantic Networks
    public function transfer()
    {
        $post = $this->input->post();
        // TODO output randomly goes to this page, unsure why, so grab it and move it.
        $files = scandir($this->config->item('base_directory'));
        $source = $this->config->item('base_directory');
        $destination = $this->file_dir . '/partiview_generator/';
        $individual_gexf_dest = $destination . 'individual_gexfs/';
        foreach ($files as $file) {
            $file_extension = pathinfo($file, PATHINFO_EXTENSION);
            if (($file == "completeLayout.gexf") || ($file == "completeLayout.pdf") || ($file == "FileDates.txt")) //Check File Extensions, transfer file to Semantic Networks if .dl
            {
                if (in_array($file, array(".", ".."))) {
                    continue;
                }

                // If we copied this successfully, mark it for deletion
                if (copy($source . $file, $destination . $file)) {
                    //$delete[] = $source.$file;
                    unlink($source . $file);
                }
            } else if ($file_extension == "gexf") //Check File Extensions, transfer file to Semantic Networks if .dl
            {
                if (in_array($file, array(".", ".."))) {
                    continue;
                }

                // If we copied this successfully, mark it for deletion
                if (copy($source . $file, $individual_gexf_dest . $file)) {
                    //$delete[] = $source.$file;
                    unlink($source . $file);
                }
            }
        }
        ///Why the extra logic here? why not simply unlink it when your code reaches the necessary part
        //foreach ($delete as $file) //Make so Files only appear in Semantic Networks, deletes them from
        //{
        //    unlink($file);
        //}
    }

    // Generate global .gexf file and .txt date file for all files imported
    public function generateGlobalGEXF()
    {
        $this->index();
        $post = $this->input->post();
        if ($this->session->userdata('logged_in')) {
            $gephi_path = $this->config->item('base_directory') . 'assets/GephiPipe/AutoGephiPipeV3_1.jar';
            $output = '';
            $cmd = '';
            $file_path = $this->file_dir . '/semantic_networks';

            $layout = $this->session->userdata('layout');
            $mod_res = $this->session->userdata('mod_resolution');
            $cmd = 'java' . ' -jar ' . $gephi_path . " " . $file_path . " " . $layout . " " . $mod_res;
            //--------debug-----------//
            echo "<script type='text/javascript'>alert('generateGlobalGEXF: $cmd');</script>";
            $message = "command: " . $cmd;
            echo "<script type='text/javascript'>alert('$message');</script>";
            $output = shell_exec($cmd);
            if ($output == '') {
                $output = "Netork Generation failed";
            }
        }

        $this->session->set_flashdata('flash_message', 'Saved to Partiview');
        $this->transfer();

        redirect('semantic_networks', 'refresh');
    }

    // Use this function to generate one individual GEXF file for each .dl file process.  These individual files will hold the modularity class information
    // for each individual layer in time for Partiview which will be key for splitting noodles.
    public function generateIndividualGEXFs()
    {
        $this->index();
        $post = $this->input->post();

        $gephi_path = $this->config->item('base_directory') . 'assets/GephiPipe/AutoGephiPipeV3_1.jar';
        $individual_gephi_path = $this->config->item('base_directory') . 'assets/GephiPipe/individualGraphProcess.jar';
        $dir_path = $this->file_dir . '/semantic_networks/';

        // Generate .gexf file for each .dl file in preprocessed directory
        if ($this->session->userdata('logged_in')) {
            $file_path = $this->file_dir . '/semantic_networks';
            // For exporting individual .gexf for each .dl
            $files = scandir($file_path);
            $cmd2 = '';
            $output2 = '';

            echo '<script type="text/javascript">alert("' . $file . '"); </script>';
            $cmd2 = 'java' . ' -jar ' . $gephi_path ." ". $dir_path . ' ' . $layout . ' ' . $mod_res;
            //$message = "command: ".$cmd2;
            echo "<script type='text/javascript'>alert('$message');</script>";
            $output2 = shell_exec($cmd2);

            /*foreach ($files as $file)
        {
        echo '<script type="text/javascript">alert("' . $file . '"); </script>';
        $cmd2='java'. ' -jar '. $individual_gephi_path. $dir_path .$file;
        //$message = "command: ".$cmd2;
        //echo "<script type='text/javascript'>alert('$message');</script>";
        $output2=shell_exec($cmd2);
        if($output2=='')
        {
        $output2="Netork Generation failed for individual file";
        }

        }*/
        }

        $this->session->set_flashdata('flash_message', 'Saved to Partiview');
        $this->transfer();

        redirect('semantic_networks', 'refresh');
    }

    public function display_file()
    {
        $file = $this->uri->segment(3);
        $file_path = $this->file_dir . "/semantic_networks/" . $file;

        echo nl2br(file_get_contents($file_path));
        exit;
    }

    public function submit_files()
    {
        if (is_null($this->input->post('checkbox'))) {
            redirect('semantic_networks', 'refresh'); //--reload the page
        } else {
            if ($this->input->post('file_action') == "delete") {
                $this->delete_files($this->input->post('checkbox'));
            } else if ($this->input->post('file_action') == "download") {
                $this->download($this->input->post('checkbox'));
            } else {
                $this->generateGlobalGEXF($this->input->post('checkbox'));
                //Call below function to generate individual GEXFs from individual .dl files
                $this->generateIndividualGEXFs($this->input->post('checkbox'));
            }
        }
    }

    public function download($files)
    {   
        if (count($files) == 1) {
            foreach ($files as $file => $file_name) {
                $file_path = $this->file_dir . '/semantic_networks/' . $file_name;
                if (file_exists($file_path)) {
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
        } else {
            $this->load->library('zip');
            foreach ($files as $file => $file_name) {
                $file_path = $this->file_dir . '/semantic_networks/' . $file_name;
                if (file_exists($file_path)) {
                    $this->zip->read_file($file_path);
                }
            }
            $this->zip->download('files.zip');
        }
        $this->index();
    }

    public function delete_files($files_to_delete)
    {
        $source = $this->file_dir . '/semantic_networks/';
        foreach ($files_to_delete as $file) {
            $delete[] = $source . $file;
        }
        foreach ($delete as $file) {
            unlink($file);
        }
        redirect('semantic_networks', 'refresh');
    }
}
