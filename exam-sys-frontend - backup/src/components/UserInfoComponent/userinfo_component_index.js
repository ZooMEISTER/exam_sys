import React, { useState, useEffect } from 'react';
import { useSelector } from "react-redux";
import { useLocation } from 'react-router-dom'
import { useNavigate } from "react-router-dom"

import { Modal, Form } from 'antd';

import './userinfo_component_index.css'

const UserInfo_Component = (props) => {

    return(
        <div>
            <Modal title={props.modalName}
                open={props.isModalOpen} 
                onCancel={props.handleCancel}
                destroyOnClose={true}
                footer={[]}
            >
                <Form name="basic"
                    labelCol={{span: 6}}
                    wrapperCol={{span: 16}}
                    style={{maxWidth: 600}}>
                    <Form.Item
                        label="头像"
                        name="avatar"
                        className='user-info-form-row'
                    >
                        <img className='user-info-detail-avatar' alt='No Img' src={props.avatar}/>
                    </Form.Item>
                    <Form.Item
                        label="用户名"
                        name="username"
                        className='user-info-form-row'
                    >
                        <label className='user-info-detail-text'>{props.username}</label> <br/>
                    </Form.Item>
                    <Form.Item
                        label="真名"
                        name="realname"
                        className='user-info-form-row'
                    >
                        <label className='user-info-detail-text'>{props.realname}</label> <br/>
                    </Form.Item>
                    <Form.Item
                        label="电话"
                        name="phone"
                        className='user-info-form-row'
                    >
                        <label className='user-info-detail-text'>{props.phone}</label> <br/>
                    </Form.Item>
                    <Form.Item
                        label="邮箱"
                        name="email"
                        className='user-info-form-row'
                    >
                        <label className='user-info-detail-text'>{props.email}</label> <br/>
                    </Form.Item>
                </Form>
            </Modal>
        </div>
    )
}

export default UserInfo_Component