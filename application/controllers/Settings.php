<?php if (!defined('BASEPATH')) {
    exit('No direct script access allowed');
}

class settings extends CI_Controller
{
    public $data;
    public $file_dir;
    public $user_info;

    public function __construct()
    {
        parent::__construct();
        $this->load->helper(array('form'));
        $this->load->model('user', '', true);
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
            $data['projects'] = $this->projects->get_projects($this->session->userdata('id'));
            $data['current_project'] = $this->projects->get_project($this->session->userdata('project_id'));

            $this->load->view('settings', $data);
        }
    }

    public function save_settings()
    {
        $input = $this->input;

        switch ($input->post('file_action')) {
            case 'net_gen_set':
                $this->save_use_freq($input->post('useFreq'));
                $this->save_low_freq($input->post('freq_lower'), $input->post('freq_upper'));
                $this->save_high_freq($input->post('freq_upper'), $input->post('freq_lower'));
                $this->save_sliding_window($input->post('sliding_window'));
                break;
            case 'net_ana_set':
                $this->save_layout($input->post('layout'));
                $this->save_mod($input->post('mod_resolution'));
                break;
            case 'net_vis_set':
                $this->save_date_range($input->post('date_range'));
                $this->save_skew_x($input->post('skew_x'));
                $this->save_skew_y($input->post('skew_y'));
                $this->save_skew_x($input->post('skew_z'));
                $this->save_shape($input->post('shape'));
                break;
            case 'current_project':
                $this->change_project($input->post('project'));
                break;
            case 'delete':
                $this->delete_project($input->post('project'));
                break;
        }

        redirect('settings', 'refresh');
    }

    public function save_use_freq($bool)
    {
        if ($bool == "on") {
            $bool = true;
        } else {
            $bool = false;
        }
        $id = $this->session->userdata('id');
        $data = array(
            'use_freq' => $bool,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('use_freq', $bool);
    }

    public function save_low_freq($low_freq, $high_freq)
    {
        $id = $this->session->userdata('id');
        if ($low_freq >= 0 && $low_freq < $high_freq) {
            $data = array(
                'freq_lower_bound' => $low_freq,
            );

            $this->db->where('id', $id);
            $this->db->update('users', $data);
            $this->session->set_userdata('freq_lower_bound', $low_freq);
        }
    }

    public function save_high_freq($high_freq, $low_freq)
    {
        $id = $this->session->userdata('id');
        if ($high_freq > $low_freq && $high_freq <= 1) {
            $data = array(
                'freq_upper_bound' => $high_freq,
            );

            $this->db->where('id', $id);
            $this->db->update('users', $data);
            $this->session->set_userdata('freq_upper_bound', $high_freq);
        }
    }

    public function save_sliding_window($sliding_window)
    {
        $id = $this->session->userdata('id');
        if ($sliding_window > 1 && $sliding_window < 6) {
            $data = array(
                'sliding_window' => $sliding_window,
            );

            $this->db->where('id', $id);
            $this->db->update('users', $data);
            $this->session->set_userdata('sliding_window', $sliding_window);
        }
    }
    /*
    Set the layout that network analysis should use when creating the graph of concepts
    
    TODO: I don't recall which of these are actually implemented - should be checked and
    the ones that aren't, removed from the options in the view
    */
    public function save_layout($layout)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'layout' => $layout,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('layout', $layout);
    }

    public function save_mod($mod_resolution)
    {
        $id = $this->session->userdata('id');
        if ($mod_resolution > 0 && $mod_resolution <= 1) {
            $data = array(
                'mod_resolution' => $mod_resolution,
            );

            $this->db->where('id', $id);
            $this->db->update('users', $data);
            $this->session->set_userdata('mod_resolution', $mod_resolution);
        }
    }

    /*
    Save the date range that concepts should be connected in between layers

    TODO: Don't think this is implemented - should either be removed, implemented,
    or disabled/noted that it isn't functional
    */
    public function save_date_range($date_range)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'date_range' => $date_range,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('date_range', $date_range);
    }

    public function save_skew_x($skew_x)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'skew_x' => $skew_x,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('skew_x', $skew_x);
    }

    public function save_skew_y($skew_y)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'skew_y' => $skew_y,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('skew_y', $skew_y);
    }

    public function save_skew_z($skew_z)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'skew_z' => $skew_z,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('skew_z', $skew_z);
    }

    /*
    Save shape of communities when generating visualization files

    TODO: Not sure which of these is implemented - need to remove, disable,
    disclaim, or implement
    */
    public function save_shape($shape)
    {
        $id = $this->session->userdata('id');
        $data = array(
            'shape' => $shape,
        );

        $this->db->where('id', $id);
        $this->db->update('users', $data);
        $this->session->set_userdata('shape', $shape);
    }

    /*
    Change the user's currently active project
    */
    public function change_project($project_id)
    {
        $user_id = $this->session->userdata['id'];
        $project = $this->projects->get_project($project_id);

        // TODO: should be removed when file_dir is taken out of the db,
        // at which point all we'll have to do is update the user's current project id,
        // and the session userdata
        $new_file_dir = $this->config->item('user_directory') . $this->session->userdata['email'];
        $new_file_dir = $new_file_dir . '/' . $project->name;
        $new_project = array(
            'project_id' => $project_id,
            'file_dir' => $new_file_dir
        );

        $this->user->update_user($new_project, $user_id);

        $this->session->set_userdata('project_id', $project_id);
        $this->session->set_userdata('file_dir', $new_file_dir);
        $this->session->set_userdata('project_name', $project->name);
    }

    /*
    Delete a project from a user's list of active projects

    TODO: If the user deletes their currently active project, we need to account
    for that somehow -- also if they delete ALL their projects
    */
    public function delete_project($id)
    {
        $this->projects->delete_project($id);
        // TODO: "Deleted" projects can be recovered, so all this should really do is
        // somehow un-link the project from the user account
        // Maybe keep a list of each user's active projects?
    }

    public function create_directory()
    {
        $dir = $this->input->post('directory_name');

        $projectIn = array(
            'name' => $dir,
            'user_id' => $this->session->userdata('id'),
        );
        $this->projects->createProject($projectIn);

        $dir_path = $this->config->item('user_directory') . $this->session->userdata('email');
        $dir = $dir_path . '/' . $dir;

        $oldmask = umask(0);
        if (!@mkdir($dir, 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        if (!@mkdir($dir . '/raw', 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        if (!@mkdir($dir . '/preprocessed', 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        if (!@mkdir($dir . '/semantic_networks', 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        if (!@mkdir($dir . '/partiview_generator', 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        // if (!@mkdir($dir . '/partiview_generator/individual_gexfs', 0777)) {
        //     $error = error_get_last();
        //     echo $error['message'];
        // }
        if (!@mkdir($dir . '/parti_output', 0777)) {
            $error = error_get_last();
            echo $error['message'];
        }
        umask($oldmask);
        redirect('settings', 'refresh');
    }
}
