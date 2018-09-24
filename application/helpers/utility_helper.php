<?php
function asset_url()
{
    return base_url() . 'assets/';
}

function style_url()
{
    return base_url() . 'application/views/styles/';
}

function flash_data($arr)
{
    if (!empty($arr['flash_message'])) {
        $html = '<p class="warning">';
        $html .= $arr['flash_message'];
        $html .= '</p>';
        echo $html;
    }
}

// Mainly used for debugging
function echo_arr($arr)
{
    foreach ($arr as $key => $value) {
        echo "{$key} => {$value} <br />";
    }
}
