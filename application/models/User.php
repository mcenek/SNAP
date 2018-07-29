<?php

class User extends CI_Model
{
    public $status;
    public $roles;

    public function __construct()
    {
        parent::__construct();
        $this->status = $this->config->item('status');
        $this->roles = $this->config->item('roles');
    }

    public function insertUser($dbIn)
    {
        $string = array(
            'firstName' => $dbIn['firstName'],
            'lastName' => $dbIn['lastName'],
            'email' => $dbIn['email'],
            'role' => $this->roles[0],
            'status' => $this->status[0],
        );
        $userIn = $this->db->insert_string('users', $string);
        $this->db->query($userIn);
        return $this->db->insert_id();
    }

    public function isDuplicate($email)
    {
        $this->db->get_where('users', array('email' => $email), 1);
        return $this->db->affected_rows() > 0 ? true : false;
    }

    public function insertToken($user_id)
    {
        //$token is just for validating a user
        //$date is used to fill in created column of 'tokens'
        //to perform a dateDiff to see validated in appropriate time frame
        $token = substr(sha1(rand()), 0, 30);
        $date = date('Y-m-d');

        $string = array(
            'token' => $token,
            'user_id' => $user_id,
            'created' => $date,
        );
        $query = $this->db->insert_string('tokens', $string);
        $this->db->query($query);
        return $token;
    }

    public function isTokenValid($token)
    {
        $q = $this->db->get_where('tokens', array('token' => $token), 1);

        if ($this->db->affected_rows() > 0) {
            $row = $q->row();

            //set to user registered date
            $created = $row->created;
            $created_time = strtotime($created);
            $now = time();
            $now_time = strtotime($now);
            $time_diff = ($now_time - $created_time) / 60 / 60;

            if ($time_diff >= 24) {
                return false;
            }

            $user_info = $this->getUserInfo($row->user_id);
            return $user_info;
        } else {
            return false;
        }
    }

    public function updateUserInfo($post, $dir)
    {

        $projectIn = array(
            'name' => 'project_one',
            'user_id' => $post['user_id'],
        );
        $this->projects->createProject($projectIn);
        $default_project = $this->projects->get_projects($post['user_id']);
        $first_row = $default_project[0];
        $update_array = array(
            'project_id' => $first_row['project_id'],
        );
        $this->update_user($update_array, $post['user_id']);

        $oldmask = umask(0);
        if (!@mkdir($dir, 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        if (!@mkdir($dir . '/project_one', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        $dir = $dir . '/project_one';
        if (!@mkdir($dir . '/raw', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        if (!@mkdir($dir . '/preprocessed', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        if (!@mkdir($dir . '/semantic_networks', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        if (!@mkdir($dir . '/partiview_generator', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }
        // if(!@mkdir($dir . '/partiview_generator/individual_gexfs', 0777)){
        //     $error = error_get_last();
        //     echo $error['message'];
        //     return false;
        // }

        if (!@mkdir($dir . '/parti_output', 0777)) {
            $error = error_get_last();
            echo $error['message'];
            return false;
        }

        umask($oldmask);

        $data = array(
            'password' => $post['password'],
            'last_login' => date('Y-m-d h:i:s A'),
            'status' => $this->status[1],
            'file_dir' => $dir,
        );
        $this->db->where('id', $post['user_id']);
        $this->db->update('users', $data);
        $success = $this->db->affected_rows();

        if (!$success) {
            error_log('Unable to updateUserInfo(' . $post['user_id'] . ')');
            return false;
        }

        $user_info = $this->getUserInfo($post['user_id']);
        return $user_info;
    }

    public function update_user($arrayIn, $idIn)
    {
        $this->db->where('id', $idIn);
        $this->db->update('users', $arrayIn);
    }

    public function getUserInfo($id)
    {
        $query = $this->db->get_where('users', array('id' => $id), 1);
        if ($this->db->affected_rows() > 0) {
            $user_info = $query->row();
            return $user_info;
        } else {
            error_log('No user found getUserInfo(' . $id . ')');
            return false;
        }
    }

    public function checkLogin($login_info)
    {
        //$this->load->library('password');
        $this->db->select('*');
        $this->db->where('email', $login_info['email']);
        $query = $this->db->get('users');
        $user_info = $query->row();
        if ($user_info) {
            //if(!$this->password->validate_password($login_info['password'], $user_info->password)){
            if (!password_verify($login_info['password'], $user_info->password)) {
                error_log('Incorrect password (' . $login_info['email'] . ')');
                return false;
            }

            $this->updateLoginTime($user_info->id);

            unset($user_info->password);
            return $user_info;
        }
    }

    public function updateLoginTime($id)
    {
        $this->db->where('id', $id);
        $this->db->update('users', array('last_login' => date('Y-m-d h:i:s A')));
        return;
    }

}
