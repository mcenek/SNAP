<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class Raw_uploads extends CI_Controller
{
    public $data;
    public $file_dir;
    public $output = '';

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
            ini_set('max_file_uploads', 20000);
            $files = array_filter(scandir($this->file_dir . '/raw'), function ($item) {
                return !is_dir($this->file_dir . '/' . $item);
            });

            $error = '';
            $user_info = array('files' => $files, 'error' => $error);

            $this->load->view('raw_uploads', $user_info);
        } else {
            redirect('home', 'refresh');
        }
    }

    public function display_file()
    {
        $file = $this->uri->segment(3);
        $file_path = $this->file_dir . "/raw/" . $file;

        $file_handle = fopen($file_path, "r");
        $file_contents = fread($file_handle, filesize($file_path));
        fclose($file_handle);

        $text_data = array('raw_text' => $file_contents,
            'output' => '',
            'file_name' => $file);

        $this->load->view('preprocess', $text_data);
    }

    public function build_command($framework, $post)
    {
        // Choose which processing framework to use then which functionality to make use of
        $cmd = '';
        if ($framework == 'corenlp') {
            if ($post['tokenize'] != '') {
                $cmd .= ' tokenize';
            }
            if ($post['sent_split'] != '') {
                $cmd .= ' sent_split';
            }
            if ($post['pos_tag'] != '') {
                $cmd .= ' pos_tag';
            }
            if ($post['lemmatize'] != '') {
                $cmd .= ' lemmatize';
            }
            if ($post['ner_tag'] != '') {
                $cmd .= ' ner_tag';
            }
            return $cmd;
        } else if ($framework == 'nltk') {
            if ($post['tokenize'] != '') {
                $cmd .= ' tokenize';
            }
            if ($post['sent_split'] != '') {
                $cmd .= ' sent_split';
            }
            if ($post['pos_tag'] != '') {
                $cmd .= ' pos_tag';
            }
            if ($post['lemmatize'] != '') {
                $cmd .= ' lemmatize';
            }
            if ($post['ner_tag'] != '') {
                $cmd .= ' ner_tag';
            }
            return $cmd;
        } else if ($framework == 'spacy') {
            if ($post['tokenize'] != '') {
                $cmd .= ' tokenize';
            }
            if ($post['sent_split'] != '') {
                $cmd .= ' sent_split';
            }
            if ($post['pos_tag'] != '') {
                $cmd .= ' pos_tag';
            }
            if ($post['lemmatize'] != '') {
                $cmd .= ' lemmatize';
            }
            if ($post['ner_tag'] != '') {
                $cmd .= ' ner_tag';
            }
            return $cmd;
        }
    }

    public function upload_text()
    {

       ini_set('max_file_uploads', 20000);

        $config['upload_path'] = $this->file_dir . '/raw';
        $config['allowed_types'] = '*';
        $config['max_size'] = '1000000';

        $files = $_FILES;
        $file_count = count($_FILES['raw_files']['name']);

        $this->load->library('upload');
        $this->upload->initialize($config);

        for ($i = 0; $i < $file_count; $i++) {
            $_FILES['raw_files']['name'] = $files['raw_files']['name'][$i];
            $_FILES['raw_files']['type'] = $files['raw_files']['type'][$i];
            $_FILES['raw_files']['tmp_name'] = $files['raw_files']['tmp_name'][$i];
            $_FILES['raw_files']['size'] = $files['raw_files']['size'][$i];
            $_FILES['raw_files']['error'] = $files['raw_files']['error'][$i];

            $path = $_FILES['raw_files']['name'];
            $ext = pathinfo($path, PATHINFO_EXTENSION);
            // to fix error with text files having characters in them, but also still filter out all non text files
            if ($ext == 'txt') {
               if ($this->upload->do_upload('raw_files')) {
                    $this->session->set_flashdata('flash_message', "Files sucessfully uploaded!");
               } else {
       		    show_error($this->upload->display_errors());
                    $error = array('error' => $this->upload->display_errors());
                    $this->load->view('raw_uploads', $error);
                }
            }
        }
        redirect('raw_uploads', 'refresh');
    }

    public function submit_files()
    {
        if (is_null($this->input->post('checkbox'))) {
            redirect('raw_uploads', 'refresh');
        } else {
            if ($this->input->post('file_action') == "delete") {
                $this->delete_files($this->input->post('checkbox'));
            } else if ($this->input->post('file_action') == "download") {
                $this->download($this->input->post('checkbox'));
            } else {
                $this->batch_preprocess($this->input->post('checkbox'));
            }
        }
    }

    public function download($files)
    {
        if (count($files) == 1) {
            foreach ($files as $file => $file_name) {
                $file_path = $this->file_dir . '/raw/' . $file_name;
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
                $file_path = $this->file_dir . '/raw/' . $file_name;
                if (file_exists($file_path)) {
                    $this->zip->read_file($file_path);
                }
            }
            $this->zip->download('files.zip');
        }
        $this->index();
    }

    public function batch_preprocess()
    {
        $files = $this->input->post('checkbox');
        $post = $this->input->post();
        $PIDArray = "{\"Pids\":[";

        if (!is_null($files)) {
            foreach ($files as $file => $file_name) {
                $preprocess_path = config_item('base_directory') . 'assets/preprocess/';
                $cmd = '';
                $file_path = $this->file_dir . '/raw/' . $file_name;

                if ($post['stemming'] != null) {
                    if ($post['stemming'] == 'porter') {
                        // TODO add something here
                    } else if ($post['stemming'] == 'porter2') {
                        // TODO
                        $cmd = 'java ' . $preprocess_path . 'stem/porter2/SOMETHING HERE ' . $file_path;
                        $output = shell_exec($cmd);
                        if ($output == '') {
                            $output = "stemming failed";
                        }
                    } else if ($post['stemming'] == 'lancaster') {
                        // TODO add something here
                    }
                }

                if ($post['tokenize'] == 'corenlp') {
                    $preprocess_path .= 'corenlp/';
                    $cmd .= 'java -cp ' . $preprocess_path . '*:';
                    $cmd .= $preprocess_path . ' StanfordCoreNlpDemo ' . $file_path;
                    $cmd .= $this->build_command('corenlp', $post);
                } else if ($post['tokenize'] == 'nltk') {
                    $preprocess_path .= 'nltk/';
                    $cmd .= 'python ' . $preprocess_path . 'nltk-nlp.py ' . $file_path;
                    $cmd .= $this->build_command('nltk', $post);
                } else if ($post['tokenize'] == 'spacy') {
                    $preprocess_path .= 'spacy/';
                    $cmd .= 'python ' . $preprocess_path . 'spacy-nlp.py ' . $file_path;
                    $cmd .= $this->build_command('spacy', $post);
                }

        		$cmd .= ' > ' . $this->file_dir . '/preprocessed/' . $file_name . ' & echo $!';
        		exec($cmd,$op);

                $output = (int) $op[0];
        		$PIDArray .= '"' . $output . '", ';
        		#$PIDArray .= '"' . $cmd . '", ';
        		#$PIDArray .= '"' . $op . '", ';
        		#$output = popen( $cmd,"r");
        		#echo "$op";

        		#while( !feof( $output ) ){
        		#	echo fread( $output, 256);
        		#	flush();
        		#	ob_flush();
        		#	#usleep(100000);
        		#}
        		#pclose($output);
            }

            $PIDArray = rtrim($PIDArray, ", ");
            $PIDArray .= ']}';
            $this->session->set_flashdata('flash_message', 'Files are processing');

            echo $PIDArray;
        } else {
            $this->session->set_flashdata('flash_message', 'Need to select a file for preprocessing.');
        }
    }

    public function kill_process()
    {
        $PIDS = $this->input->post('PIDS');
        $files = $this->input->post('checkbox');
        foreach ($PIDS as &$PID) {
            exec('kill ' . $PID);
        }
        foreach ($files as $file => $file_name) {
            exec('rm ' . $this->file_dir . '/preprocessed/' . $file_name);
        }
    }

    public function delete_files($files_to_delete)
    {
        $source = $this->file_dir . '/raw/';
        foreach ($files_to_delete as $file) {
            $delete[] = $source . $file;
        }
        foreach ($delete as $file) {
            unlink($file);
        }
        redirect('raw_uploads', 'refresh');
    }
}
