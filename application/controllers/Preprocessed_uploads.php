<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Preprocessed_uploads extends CI_Controller
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
            $files = array_filter(scandir($this->file_dir . '/preprocessed'),
                function ($item) {
                    return !is_dir($this->file_dir . '/' . $item);
                });
            $error = '';
            $user_info = array('files' => $files, 'error' => $error);
            $this->load->view('preprocessed_uploads', $user_info);
        }
    }

    // TODO: Which of these isn't actually needed? I don't the this comment is accurate...
    //depricated doesn't no longer needed
    public function netgen()
    {
        $files = $this->input->post('checkbox');
        $PIDArray = "{\"Pids\":[";
        $source = $this->file_dir . '/preprocessed/';
        $destination = $this->file_dir . '/semantic_networks/';
        $use_freq = $this->session->userdata('use_freq');
        $freq_lower = $this->session->userdata('freq_lower_bound');
        $freq_upper = $this->session->userdata('freq_upper_bound');
        $cmd = '';
        foreach ($files as $file => $file_name) {
            $netgen_path = $this->config->item('base_directory') . 'assets/NetGen/';
            $output = '';
            $file_path = $this->file_dir . '/preprocessed/' . $file_name . ' ';
            //-------------------Generate .dl files for every file in preprocessed directory----------------------------------//
            // run the java command, redirect the outputs to null so that doesn't block echo, the java does it's own file io to the dir, echo back the PID
            $cmd = 'nice java' . ' -jar ' . $netgen_path . 'NetGenL4.jar ' . $file_path . $netgen_path . 'stopword.txt' . ' ' . $use_freq . ' ' . $freq_lower . ' ' . $freq_upper . ' 2>&1 1> /dev/null & echo $!';
            //$cmd = 'nice java' . ' -jar ' . $netgen_path . 'NetGenL4.jar ' . $file_path . $netgen_path . 'stopword.txt 2>&1 1> /dev/null & echo $!';

            //--------debug-----------//
            //$message = "command: ".$cmd;
            $output = trim(shell_exec($cmd));

            //$output = (int)$op[0];

            $PIDArray .= '"' . $output . '", ';

        }
        //echo "<script type='text/javascript'>alert('NetGenL4: $cmd');</script>";
        // while the PID exists pause/sleep, when nothing returns the PID is dead so move the files that have been created by the java process
        $cmd2 = 'while kill -0 ' . $output . '
				do
				sleep 1
				done
				mv ' . '/' . $source . '/*.dl ' . $destination . '&';

        exec($cmd2);
        $PIDArray = rtrim($PIDArray, ", ");
        $PIDArray .= ']}';
        echo $PIDArray;
    }

    //function that is called to generate the .dl files for netword analysis
  //   public function netgen2()
  //   {
  //       $files = $this->input->post('checkbox');
  //       $PIDArray = "{\"Pids\":[";
  //       $source = $this->file_dir . '/preprocessed/';
  //       $destination = $this->file_dir . '/semantic_networks/';
  //       $cmd = '';
  //       foreach ($files as $file => $file_name) {
  //           $netgen_path = $this->config->item('base_directory') . 'assets/NetGen/';
  //           $output = '';

  //           $file_path = $this->file_dir . '/preprocessed/' . $file_name . ' ';
  //           //-------------------Generate .dl files for every file in preprocessed directory----------------------------------//
  //           $use_freq = $this->session->userdata('use_freq');

  //           $freq_lower = $this->session->userdata('freq_lower_bound');
  //           $freq_upper = $this->session->userdata('freq_upper_bound'); //put this here to keep it the same with netgen
  //           $cmd = 'java' . ' -jar ' . $netgen_path . 'NetGenL4.jar ' . $file_path . $netgen_path . 'stopword.txt' . ' ' . $use_freq . ' ' . $freq_lower . ' ' . $freq_upper . ' 2>&1 1> /dev/null & echo $!';
  //           //--------debug-----------//
  //           //$message = "command: ".$cmd;
  //           $output = trim(shell_exec($cmd));
  //           //$output = (int)$op[0];
  //           $PIDArray .= '"' . $output . '", ';

  //       }

  //       $cmd2 = 'while kill -0 ' . $output . '
		// do
		// sleep 1
		// done
		// mv ' . '/' . $source . '/*.dl ' . $destination . '';

  //       exec($cmd2);
  //       $PIDArray = rtrim($PIDArray, ", ");
  //       $PIDArray .= ']}';
  //       echo $PIDArray;
  //   }

    public function HistoGen()
    {
        $files = $this->input->post('checkbox');
        if (!is_null($files)) {
            $PIDArray = "{\"Pids\":[";
            $source = $this->file_dir . '/preprocessed/';
            $destination = $this->file_dir . '/semantic_networks/';
            foreach ($files as $file => $file_name) {
                $histGen_path = 'assets/HistGen/';
                #$histGen_path = '/Applications/MAMP/htdocs/website_stuff/assets/HistGen/';
                $output = '';
                $cmd = '';
                $file_path = $this->file_dir . '/preprocessed/' . $file_name . ' ';

                // Generate histogramFrequencies for every file in preprocessed directory
                $cmd = 'java' . ' -jar ' . $histGen_path . "HistGen.jar " . $file_path . $histGen_path . 'stopword.txt ' . 1;

                exec($cmd);
            }

            $PIDArray = "{\"Frequency\":[";
            $file_path = $this->file_dir . '/preprocessed/';
            exec('sleep 1');
            // concatanates all the files into one file
            $cmd2 = 'cat ' . $file_path . '*Histogram*.txt > ' . $file_path . 'histo.txt';
            exec($cmd2);

            exec('sleep 1');

            // removes the histogram files generated by java
            $cmd3 = 'rm ' . $file_path . '*Histogram*.txt';
            exec($cmd3);

            exec('sleep 1');

            // gets the unique counts of each diffent variable count and sorts before returning it into
            exec("sort -n " . $file_path . "histo.txt | uniq -c | awk '{print $1}' | sort -n > " . $file_path . "data.txt");

            $handle = fopen($file_path . "data.txt", "r");
            if ($handle) {
                while (($line = fgets($handle)) !== false) {
                    $PIDArray .= '"' . $line . '", ';
                }
            }

            exec('sleep 1');

            //removes the histogram files generated by java
            $cmd5 = 'rm ' . $file_path . 'histo.txt ' . $file_path . 'data.txt';
            exec($cmd5);

            $PIDArray = rtrim($PIDArray, ", ");
            $PIDArray .= ']}';
            $PIDArray = trim(preg_replace('/\s+/', '', $PIDArray));
            echo $PIDArray;
        }
    }

    public function display_file()
    {
        $file = $this->uri->segment(3);
        $file_path = $this->file_dir . "/preprocessed/" . $file;
        $file_parts = pathinfo($file);
        //Check File Extensions, transfer file to Semantic Networks if .dl
        if ($file_parts['extension'] == "txt") {
            echo nl2br(file_get_contents($file_path));
            exit;
        } else {
            exit;
        }
    }

    public function submit_files()
    {
        if (is_null($this->input->post('checkbox'))) {
            redirect('preprocessed_uploads', 'refresh'); //--reload the page
        } else {
            if ($this->input->post('file_action') == "delete") {
                $this->delete_files($this->input->post('checkbox'));
            } else if ($this->input->post('file_action') == "download") {
                $this->download($this->input->post('checkbox'));
            } else {
                $this->netgen($this->input->post('checkbox'));
            }
        }
    }

    public function kill_process()
    {
        $PIDS = $this->input->post('PIDS');

        foreach ($PIDS as &$PID) {
            exec('kill ' . $PID);
        }
    }

    public function download($files)
    {
        if (count($files) == 1) {
            foreach ($files as $file => $file_name) {
                $file_path = $this->file_dir . '/preprocessed/' . $file_name;
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
                $file_path = $this->file_dir . '/preprocessed/' . $file_name;
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
        $source = $this->file_dir . '/preprocessed/';
        foreach ($files_to_delete as $file) {
            $delete[] = $source . $file;
        }
        foreach ($delete as $file) {
            unlink($file);
        }
        redirect('preprocessed_uploads', 'refresh');
    }
}
