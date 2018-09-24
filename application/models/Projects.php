<?php

class Projects extends CI_Model
{
    public function createProject($projectIn)
    {
        $string = array(
            'name' => $projectIn['name'],
            'user_id' => $projectIn['user_id'],
        );
        $projectIn = $this->db->insert('projects', $string);
    }

    public function get_projects($id)
    {
        $query = $this->db->get_where('projects', array('user_id' => $id));
        return $query->result_array();
    }

    public function get_project($id)
    {
        $query = $this->db->get_where('projects', array('project_id' => $id));
        return $query->row();
    }

    public function delete_project($id)
    {
        $this->db->where('project_id', $id);
        $this->db->delete('projects');
    }
}
