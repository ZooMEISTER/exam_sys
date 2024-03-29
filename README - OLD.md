# **基于加密传输方案的远程考试系统**

## 系统概况设计

### 系统概况说明

​	通过网络形式进行远程考试的现象越来越普遍. 本课题设计一种基于加密传输方案的远程考试系统. 考试前10到15天，学生下载所有考试的AES加密试卷。学生有足够的时间，所以不必担心网络速度慢。在每次考试即将开始之前，老师将通过短信发送密码以解锁该试卷，该应用程序将解密该试卷，以便学生现在可以查看并开始考试。编写完答题纸后，学生将创建一个pdf文件，然后该应用程序将为其创建一个SHA-256哈希值。一旦所有考试都结束，学生就可以花费大量时间上载pdf答案纸，这样对于网速慢的用户不会造成任何不便。

组成部分：该系统包含一个完整的包含前端，后端的网站，和一个用于在本地解密试卷，对PDF答题纸创建SHA-256并上传该值的应用程序。

技术栈：React + SpringBoot + Redis + MybatisPlus

技术路线：JWT 方案处理用户鉴权部分

关键部分：用户系统，AES加密，短信验证系统，PDF生成，高并发

开发路线：用户系统 >> 出卷系统 >> 手动批卷 >> 自动批卷

#### 注意点

1. 老师在发布试卷时可选择自动批卷或是手动批卷。**自动批卷 **模式下，在老师发布考试后，学生无法提前下载试卷，而只能在考试时间到了以后在网站上做题，批卷环节则是在学生交卷后由考试系统自动比对答案，结出得分。**手动批卷** 模式下，老师在发布考试后，学生便可提前下载试卷 (试卷由服务端自动生成)，但试卷由AES加密故无法查看。当考试时间到达后，学生会收到包含AES密钥的短信，学生通过密钥使用本地的应用程序解锁试卷并完成试卷。编写完答题纸 (PDF格式) 后，学生使用本地的应用程序对答题纸PDF文件操作，该本地应用程序将会根据文件信息生成一个SHA-256值并上传 (注意：这里仅上传SHA-256的值，不上传文件)，随后学生在网页端上传答题纸的PDF文件，服务端在PDF上传完成后会对上传的文件再生成一次SHA-256值，并与之前由本地应用程序上传的值进行比较，若值相同，则交卷成功，若不同则该答卷作废，批卷环节则是得由老师在网站上查看PDF文件，最后输入总得分。
2. 批卷模式为自动批卷的考试会在考试结束时自动强制交卷，而手动批卷模式的考试需要考试结束后手动上传答题纸，如不上传答题纸，老师方面可直接打 0 分
3. 该系统有试卷库和题库，老师在创建考试时需要从试卷库中选择试卷，试卷需要被发布后老师才能在创建考试时选择。老师在创建试卷时可从题库中选择题目，也可直接手动添加题目，手动添加的题目在试卷被发布后会自动被添加进题库中，试卷在发布后被添加进试卷库中。未发布的试卷内容可以被修改，已发布的试卷内容不可被修改 (任何信息都不能修改)。已发布的考试可以取消但不可被修改 (任何信息都不能修改)。



### 用户系统角色设计

| 角色       | 数量 | 职能                 | 说明                                                       | 权限等级 |
| ---------- | ---- | -------------------- | ---------------------------------------------------------- | -------- |
| 超级管理员 | 1    | 管理 管理员 及其权限 | 拥有管理员，老师，学生的所有权限                           | 4        |
| 管理员     | 1+   | 管理老师，学生       | 审核老师对课程的管理请求，拥有老师，学生的部分权限         | 3        |
| 老师       | 1+   | 管理课程，学生，考试 | 管理课程时如对课程进行增删，需向管理员发申请，待管理员审核 | 2        |
| 学生       | 1+   | 可参加课程，考试     | 参加课程需要老师生成的验证码，参加考试需要老师的验证码     | 1        |

### 考试系统角色设计

| 角色 | 说明                                                         |
| ---- | ------------------------------------------------------------ |
| 学院 | 一个学院可以有多个专业，一个专业只能位于一个学院下           |
| 专业 | 一个专业只能位于一个学院下，一个专业可以有多个课程           |
| 课程 | 一个课程可以位于多个专业下，一个课程可以有多场考试，一个老师可以创建多个课程，一个课程只能对应一个老师 |
| 考试 | 一场考试只能位于一个课程下，一场考试只能有一张试卷           |
| 试卷 | 一张试卷可位于多场考试下（可跨专业，跨学院）                 |
| 题目 | 一道题目可位于多张试卷中，题目分为 选择，判断，填空，简答    |



## 前端设计

### 自动登录设计

此项目采用的是JWT，因此会在本地保存一个token，之后每次自动登录时都将这个token发送到后端进行验证。

### Redux持久化

不同于Vuex，Redux在页面刷新后不会保存状态，因此需要对Redux的状态数据进行持久化处理。采用的方案是登录时将Redux的数据以json格式存入localStorage，之后每次页面加载直接从localStorage中加载数据到Redux中。由于localStorage是永久的，故在存储时往json中加入过期时间，之后从localStoage中加载Redux数据前先判断是否过期，若过期则需使用token发送到后端的方式重新登录。

### 页面路由结构设计

```
BasePage
	FrontPage
	HomePage
		Teacher_OperateClass
		Teacher_MyClass
		Teacher_MyExam
		Student_ChooseClass
		Student_MyClass
		Student_MyExam
	LoginPage
	RegisterPage
	ProfilePage
```

### JWT Token

包含两个字段，一个是用户的 id，一个是用户的个人信息版本号。



## 数据库设计 ( Redis 7 )

- 用户登录后存入用户的id(key)和用户的权限等级，拦截器对token解析鉴权时先访问Redis，若没有记录再去查询Mysql，若查到则将对应记录加入Redis



## 数据库设计 ( MySql 8 )

### 数据库表设计 - 用户系统角色

#### 	超级管理员 ( user_super_admim )

| 字段名            | 类型   | 不为NULL | 是否主键 | 说明                                                         |
| ----------------- | ------ | -------- | -------- | ------------------------------------------------------------ |
| id                | bigint | 是       | 是       | 0                                                            |
| avatar            | string | 是       |          | 用户头像                                                     |
| username (用户名) | string | 是       |          | admin                                                        |
| realname (姓名)   | string | 是       |          | 用户真名，可重复                                             |
| password (密码)   | string | 是       |          | MD5加密，数据库里存放的是密文                                |
| phone (手机号)    | string | 是       |          | 初次注册后需验证手机号码才能使用考试系统的功能，可用于找回密码 |
| email             | string | 否       |          | 邮箱，用于接收网站的消息                                     |
| deleted           | int    | 是       |          | 用户是否删除                                                 |
| profilev          | int    | 是       |          | 表示用户信息版本，使用token自动登录时，会比对token和数据库里的该字段值，不同则需重新登录 |

#### 	管理员 ( user_admin )

| 字段名            | 类型   | 不为NULL | 是否主键 | 说明                                                         |
| ----------------- | ------ | -------- | -------- | ------------------------------------------------------------ |
| id                | bigint | 是       | 是       | 雪花算法                                                     |
| avatar            | string | 是       |          | 用户头像                                                     |
| username (用户名) | string | 是       |          | 不可重复                                                     |
| realname (姓名)   | string | 是       |          | 用户真名，可重复                                             |
| password (密码)   | string | 是       |          | MD5加密，数据库里存放的是密文                                |
| phone (手机号)    | string | 否       |          | 初次注册后需验证手机号码才能使用考试系统的功能，可用于找回密码 |
| email             | string | 否       |          | 邮箱，用于接收网站的消息                                     |
| deleted           | int    | 是       |          | 用户是否删除                                                 |
| profilev          | int    | 是       |          | 表示用户信息版本，使用token自动登录时，会比对token和数据库里的该字段值，不同则需重新登录 |

#### 	老师 ( user_teachar )

| 字段名            | 类型   | 不为NULL | 是否主键 | 说明                                                         |
| ----------------- | ------ | -------- | -------- | ------------------------------------------------------------ |
| id                | bigint | 是       | 是       | 雪花算法                                                     |
| avatar            | string | 是       |          | 用户头像                                                     |
| username (用户名) | string | 是       |          | 不可重复                                                     |
| realname (姓名)   | string | 是       |          | 用户真名，可重复                                             |
| password (密码)   | string | 是       |          | MD5加密，数据库里存放的是密文                                |
| phone (手机号)    | string | 否       |          | 初次注册后需验证手机号码才能使用考试系统的功能，可用于找回密码 |
| email             | string | 否       |          | 邮箱，用于接收网站的消息                                     |
| deleted           | int    | 是       |          | 用户是否删除                                                 |
| profilev          | int    | 是       |          | 表示用户信息版本，使用token自动登录时，会比对token和数据库里的该字段值，不同则需重新登录 |

#### 	学生 ( user_student )

| 字段名            | 类型   | 不为NULL | 是否主键 | 说明                                                         |
| ----------------- | ------ | -------- | -------- | ------------------------------------------------------------ |
| id                | bigint | 是       | 是       | 雪花算法                                                     |
| avatar            | string | 是       |          | 用户头像                                                     |
| username (用户名) | string | 是       |          | 不可重复                                                     |
| realname (姓名)   | string | 是       |          | 用户真名可重复                                               |
| password (密码)   | string | 是       |          | MD5加密，数据库里存放的是密文                                |
| phone (手机号)    | string | 否       |          | 初次注册后需验证手机号码才能使用考试系统的功能，可用于找回密码 |
| email             | string | 否       |          | 邮箱，用于接收网站的消息                                     |
| deleted           | int    | 是       |          | 用户是否删除                                                 |
| profilev          | int    | 是       |          | 表示用户信息版本，使用token自动登录时，会比对token和数据库里的该字段值，不同则需重新登录 |



### 数据库表设计 - 考试系统角色

#### 	学院 ( sys_department )

| 字段名        | 类型   | 不为NULL | 是否主键 | 说明                       |
| ------------- | ------ | -------- | -------- | -------------------------- |
| id            | bigint | 是       | 是       | 雪花算法                   |
| icon          | string | 否       |          | 学院图片，存储的是图片路径 |
| name          | string | 是       |          | 学院名，不可重复           |
| description   | string | 否       |          | 该学院的描述               |
| subjuct_count | int    | 是       |          | 该学院下属的专业数         |
| deleted       | int    | 是       |          | 学院是否删除               |

#### 	专业 ( sys_subject )

| 字段名       | 类型   | 不为NULL | 是否主键 | 说明                       |
| ------------ | ------ | -------- | -------- | -------------------------- |
| id           | bigint | 是       | 是       | 雪花算法                   |
| icon         | string | 否       |          | 专业图片，存储的是图片路径 |
| name         | string | 是       |          | 专业名，不可重复           |
| description  | string | 否       |          | 该专业的描述               |
| belongto     | bigint | 是       |          | 该专业所属的的学院的id     |
| course_count | int    | 是       |          | 该专业下属的课程数         |
| deleted      | int    | 是       |          | 专业是否删除               |

#### 	课程 ( sys_course )

| 字段名       | 类型     | 不为NULL | 是否主键 | 说明                                  |
| ------------ | -------- | -------- | -------- | ------------------------------------- |
| id           | bigint   | 是       | 是       | 雪花算法                              |
| icon         | string   | 否       |          | 课程图片，存储的是图片路径            |
| name         | string   | 是       |          | 课程名，可重复 (同一门课程会开多个课) |
| description  | string   | 否       |          | 该课程的描述                          |
| teachby      | bigint   | 是       |          | 该教授该课程的老师的 id               |
| created_time | datetime | 是       |          | 该课程创建的时间                      |
| deleted      | int      | 是       |          | 课程是否删除                          |

#### 	考试 ( sys_exam )

| 字段名       | 类型     | 不为NULL | 是否主键 | 说明                                             |
| ------------ | -------- | -------- | -------- | ------------------------------------------------ |
| id           | bigint   | 是       | 是       | 雪花算法                                         |
| name         | string   | 是       |          | 考试名，可重复                                   |
| description  | string   | 是       |          | 该考试的描述                                     |
| start_time   | datetime | 是       |          | 考试的开始时间                                   |
| end_time     | datetime | 是       |          | 考试的结束时间                                   |
| teachby      | bigint   | 是       |          | 发布该考试的老师的 id                            |
| type         | int      | 是       |          | 该考试的类型，0 为自动批卷，1 为手动批卷         |
| published    | int      | 是       |          | 表示该考试是否被发布，0 表示未发布，1 表示已发布 |
| created_time | datetime | 是       |          | 该考试创建的时间                                 |

#### 	试卷 ( sys_paper )

| 字段名       | 类型     | 不为NULL | 是否主键 | 说明                                             |
| ------------ | -------- | -------- | -------- | ------------------------------------------------ |
| id           | bigint   | 是       | 是       | 雪花算法                                         |
| name         | string   | 是       |          | 试卷名，可重复                                   |
| description  | string   | 是       |          | 该试卷的描述                                     |
| teachby      | bigint   | 是       |          | 创建该试卷的老师的 id                            |
| score        | int      | 是       |          | 该试卷的满分分数 (根据题目改变)                  |
| published    | int      | 是       |          | 表示该试卷是否被发布，0 表示未发布，1 表示已发布 |
| created_time | datetime | 是       |          | 该试卷创建的时间                                 |

说明：上述表为考试系统角色

------

#### 	题目 - 选择题 ( sys_multiple_choice_question )

| 字段名        | 类型   | 不为NULL | 是否主键 | 说明                                |
| ------------- | ------ | -------- | -------- | ----------------------------------- |
| id            | bigint | 是       | 是       | 雪花算法                            |
| title         | string | 否       |          | 题目的标题 (可为空)                 |
| content       | string | 是       |          | 题目的内容 (不可为空)               |
| contain_image | int    | 否       |          | 题目包含图片的数量，0表示没有包含， |
| answer        | string | 是       |          | 题目的答案                          |
| option_1      | string | 是       |          | 题目的其中一个选项                  |
| option_2      | string | 是       |          | 题目的其中一个选项                  |
| option_3      | string | 是       |          | 题目的其中一个选项                  |
| option_4      | string | 是       |          | 题目的其中一个选项                  |

#### 	题目 - 填空题 ( sys_blank_fill_question )

| 字段名        | 类型   | 不为NULL | 是否主键 | 说明                              |
| ------------- | ------ | -------- | -------- | --------------------------------- |
| id            | bigint | 是       | 是       | 雪花算法                          |
| title         | string | 否       |          | 题目的标题 (可为空)               |
| content       | string | 是       |          | 题目的内容 (不可为空)             |
| contain_image | int    | 否       |          | 题目包含图片的数量，0表示没有包含 |
| answer        | string | 是       |          | 题目的答案                        |

#### 	题目 - 判断题 ( sys_true_false_question )

| 字段名        | 类型   | 不为NULL | 是否主键 | 说明                              |
| ------------- | ------ | -------- | -------- | --------------------------------- |
| id            | bigint | 是       | 是       | 雪花算法                          |
| title         | string | 否       |          | 题目的标题 (可为空)               |
| content       | string | 是       |          | 题目的内容 (不可为空)             |
| contain_image | int    | 否       |          | 题目包含图片的数量，0表示没有包含 |
| answer        | int    | 是       |          | 题目的答案 (0表示错误，1表示正确) |

#### 	题目 - 简答题 ( sys_short_answer_question )

| 字段名        | 类型   | 不为NULL | 是否主键 | 说明                              |
| ------------- | ------ | -------- | -------- | --------------------------------- |
| id            | bigint | 是       | 是       | 雪花算法                          |
| title         | string | 否       |          | 题目的标题 (可为空)               |
| content       | string | 是       |          | 题目的内容 (不可为空)             |
| contain_image | int    | 否       |          | 题目包含图片的数量，0表示没有包含 |



### 数据库表设计 - 角色间关系

#### 	专业 - 课程 对应表 ( relation_subject_course )

| 字段名     | 类型   | 不为NULL | 是否主键 | 说明                |
| ---------- | ------ | -------- | -------- | ------------------- |
| id         | bigint | 是       | 是       | 雪花算法            |
| subject_id | bigint | 是       |          | 对应关系中专业的 id |
| course_id  | bigint | 是       |          | 对应关系中课程的 id |

#### 	课程 - 考试 对应表 ( relation_course_exam )

| 字段名    | 类型   | 不为NULL | 是否主键 | 说明                |
| --------- | ------ | -------- | -------- | ------------------- |
| id        | bigint | 是       | 是       | 雪花算法            |
| course_id | bigint | 是       |          | 对应关系中课程的 id |
| exam_id   | bigint | 是       |          | 对应关系中考试的 id |

#### 	考试 - 试卷 对应表 ( relation_exam_paper )

| 字段名   | 类型   | 不为NULL | 是否主键 | 说明                |
| -------- | ------ | -------- | -------- | ------------------- |
| id       | bigint | 是       | 是       | 雪花算法            |
| exam_id  | bigint | 是       |          | 对应关系中考试的 id |
| paper_id | bigint | 是       |          | 对应关系中试卷的 id |

#### 	试卷 - 题目 对应表 ( relation_paper_question )

| 字段名      | 类型   | 不为NULL | 是否主键 | 说明                     |
| ----------- | ------ | -------- | -------- | ------------------------ |
| id          | bigint | 是       | 是       | 雪花算法                 |
| paper_id    | bigint | 是       |          | 对应关系中试卷的 id      |
| question_id | bigint | 是       |          | 对应关系中题目的 id      |
| question_no | int    | 是       |          | 该题目在对应试卷中的题号 |
| score       | int    | 是       |          | 改题目在该试卷中的分数   |

#### 课程 - 学生 对应表 ( relation_course_student )

| 字段名     | 类型     | 不为NULL | 是否主键 | 说明                 |
| ---------- | -------- | -------- | -------- | -------------------- |
| id         | bigint   | 是       | 是       | 雪花算法             |
| course_id  | bigint   | 是       |          | 对应关系中课程的 id  |
| student_id | bigint   | 是       |          | 对应关系中学生的 id  |
| join_time  | datetime | 是       |          | 学生加入该课程的时间 |

**说明**：上述表表示考试系统间角色关系

------

#### 学生答案表 - 选择题 ( answer_result_multiple_choice_question )

| 字段名         | 类型   | 不为NULL | 是否主键 | 说明                   |
| -------------- | ------ | -------- | -------- | ---------------------- |
| id             | bigint | 是       | 是       | 雪花算法               |
| student_id     | bigint | 是       |          | 做这题的学生的 id      |
| exam_id        | bigint | 是       |          | 该题所在考试的 id      |
| question_id    | bigint | 是       |          | 该题的 id              |
| answer         | string | 是       |          | 该学生回答这题时的答案 |
| answer_correct | string | 是       |          | 该选择题的答案         |
| score_mine     | int    | 是       |          | 学生在这道题得到的分数 |
| score_full     | int    | 是       |          | 该题目的满分分数       |

#### 学生答案表 - 填空题 ( answer_result_blank_fill_question )

| 字段名         | 类型   | 不为NULL | 是否主键 | 说明                   |
| -------------- | ------ | -------- | -------- | ---------------------- |
| id             | bigint | 是       | 是       | 雪花算法               |
| student_id     | bigint | 是       |          | 做这题的学生的 id      |
| exam_id        | bigint | 是       |          | 该题所在考试的 id      |
| question_id    | bigint | 是       |          | 该题的 id              |
| answer         | string | 是       |          | 该学生回答这题时的答案 |
| answer_correct | string | 是       |          | 该填空题的答案         |
| score_mine     | int    | 是       |          | 学生在这道题得到的分数 |
| score_full     | int    | 是       |          | 该题目的满分分数       |

#### 学生答案表 - 判断题 ( answer_result_true_false_question )

| 字段名         | 类型   | 不为NULL | 是否主键 | 说明                   |
| -------------- | ------ | -------- | -------- | ---------------------- |
| id             | bigint | 是       | 是       | 雪花算法               |
| student_id     | bigint | 是       |          | 做这题的学生的 id      |
| exam_id        | bigint | 是       |          | 该题所在考试的 id      |
| question_id    | bigint | 是       |          | 该题的 id              |
| answer         | int    | 是       |          | 该学生回答这题时的答案 |
| answer_correct | int    | 是       |          | 该判断题的答案         |
| score_mine     | int    | 是       |          | 学生在这道题得到的分数 |
| score_full     | int    | 是       |          | 该题目的满分分数       |

#### 学生答案表 - 简答题 ( answer_short_answer_question )

| 字段名      | 类型   | 不为NULL | 是否主键 | 说明                   |
| ----------- | ------ | -------- | -------- | ---------------------- |
| id          | bigint | 是       | 是       | 雪花算法               |
| student_id  | bigint | 是       |          | 做这题的学生的 id      |
| exam_id     | bigint | 是       |          | 该题所在考试的 id      |
| question_id | bigint | 是       |          | 该题的 id              |
| answer      | string | 是       |          | 该学生回答这题时的答案 |

**说明**：上述表为答案表，在学生提交试卷时就已经生成。其中 选择，填空，判断 需要在后端进行一次简单的判断，得出最后学生得到的分数后再存入数据库中，而简答题则是直接存入，因简答题需要老师手动批阅，见下方的 **试卷批阅表 - 简答题**

------

#### 试卷批阅表 - 简答题 ( result_short_answer_question )

| 字段名      | 类型   | 不为NULL | 是否主键 | 说明                           |
| ----------- | ------ | -------- | -------- | ------------------------------ |
| id          | bigint | 是       | 是       | 雪花算法                       |
| student_id  | bigint | 是       |          | 做这题的学生的 id              |
| exam_id     | bigint | 是       |          | 该题所在考试的 id              |
| question_id | bigint | 是       |          | 该题的 id                      |
| answer      | string | 是       |          | 该学生回答这题时的答案         |
| score_mine  | int    | 是       |          | 学生在这道题得到的分数         |
| score_full  | int    | 是       |          | 该题目的满分分数               |
| comment     | string | 否       |          | 老师对该题目答案的批注，可为空 |

**说明**：上表为老师批改学生答案后才生成。老师批改时查看的是 **学生答案表 - 简答题** 中查询的数据。上表和上一节中的四张表只有在该考试的批卷模式是自动批卷时才会涉及。

------

#### 学生考试记录表 ( history_exam_student )

| 字段名        | 类型     | 不为NULL | 是否主键 | 说明                                                         |
| ------------- | -------- | -------- | -------- | ------------------------------------------------------------ |
| id            | bigint   | 是       | 是       | 雪花算法                                                     |
| student_id    | bigint   | 是       |          | 参与考试的学生的 id                                          |
| exam_id       | bigint   | 是       |          | 该考试的 id                                                  |
| enter_time    | datetime | 是       |          | 学生开始考试的时间 (进场时间)                                |
| leave_time    | datetime | 是       |          | 学生交卷的时间 (离场时间)                                    |
| type          | int      | 是       |          | 该次考试的类型，0 为自动批卷，1 为手动批卷                   |
| answer_submit | int      | 是       |          | 表示该学生参加的这场考试有没有交答卷，0 表示 未交卷，1 表示已交卷 |
| score_mine    | int      | 是       |          | 学生的分数                                                   |
| score_full    | int      | 是       |          | 该次考试的满分                                               |

**说明**：上表记录在学生点击交卷后生成 (自动批卷模式)，或是在考试结束时自动生成 (手动批卷模式，该模式中自动生成的记录的 enter_time 为考试开始时间，leave_time 为考试结束时间)

------

#### 学生答卷表 ( respondent_exam_student )

| 字段名          | 类型   | 不为NULL | 是否主键 | 说明                    |
| --------------- | ------ | -------- | -------- | ----------------------- |
| id              | bigint | 是       | 是       | 雪花算法                |
| student_id      | bigint | 是       |          | 参与考试的学生的 id     |
| exam_id         | bigint | 是       |          | 该考试的 id             |
| respondent_path | string | 是       |          | 学生上传的PDF答卷的路径 |

**说明**：该记录在学生的答题卷上传完成时生成，答卷会存放在服务端本地，老师可从网页上查看答卷并打分。该表只在考试模式为手动批卷的考试中会涉及。



## 后端模块设计

### Conatants

这里存放一些常量

### POJO.PO

这里存放与数据库对应的实体类。

- SuperAdminPO
- AdminPO
- TeacherPO
- StudentPO
- DepartmentPO
- SubjectPO
- CoursePO
- ExamPO
- PaperPO
- MultipleChoiceQuestionPO
- BlankFillQuestionPO
- TrueFalseQuestionPO
- ShortAnswerQuestionPO

### POJO.BO

这里存放业务关系对象

- RelationSubjectCourseBO
- RelationCourseExamBO
- RelationExamPaperBO
- RelationPaperQuestionBO
- RelationCourseStudentBO
- AnswerResultMultipleChoiceQuestionBO
- AnswerResultBlankFillQuestionBO
- AnswerResultTrueFalseQuestionBO
- AnswerShortAnswerQuestionBO
- ResultShortAnswerQuestionBO
- HistoryExamStudentBO
- RespondentExamStudentBO

### POJO.VO

这里存放返回给前端的实体类，基本与 PO，BO 对应。

------

### Util

这里存放后端所需要用到的一些工具类、方法。

- JWT 相关
- AES 加密相关
- SHA-256 签名相关
- 雪花算法相关

### Interceptor

- 包含用户鉴权相关逻辑，游客访问不会被拦截

------

### Controller 层

- TouristController：未登录用户访问的Controller，包含用户的登陆注册等
- UserController：包含 用户 信息修改相关接口
- SchoolController：包含 学院，专业，课程 之间的关系以及增删改查相关接口，报课退课 相关接口
- ExamController：包含 考试，题目 相关增删改查相关接口，试卷批改相关接口，参加考试，查看结果相关接口

### Service 层

- TouristService：游客相关接口，注册，登录
- UserService
- SchoolService
- ExamService

### Mapper 层

- TouristMapper：处理游客的注册
- SuperAdminMapper
- AdminMapper
- TeacherMapper
- StudentMapper
- DepartmentMapper
- SubjectMapper
- CourseMapper
- ExamMapper
- PaperMapper
- MultipleChoiceQuestionMapper
- BlankFillQuestionMapper
- TrueFalseQuestionMapper
- ShortAnswerQuestionMapper
- RelationSubjectCourseMapper
- RelationCourseExamMapper
- RelationExamPaperMapper
- RelationPaperQuestionMapper
- RelationCourseStudentMapper
- AnswerResultMultipleChoiceQuestionMapper
- AnswerResultBlankFillQuestionMapper
- AnswerResultTrueFalseQuestionMapper
- AnswerShortAnswerQuestionMapper
- ResultShortAnswerQuestionMapper
- HistoryExamStudentMapper
- RespondentExamStudentMapper



## 后端接口设计

接口根据发起的角色分类：

- 超级管理员调用的接口以 /superadmin 开头
- 管理员调用的接口以 /admin 开头
- 老师调用的接口以 /teacher 开头
- 游客调用的接口 /tourist 开头

拦截器拦截除游客外的请求，解析 token ，从 Redis 取出用户信息，对解析得出的值进行校验。



## 踩坑记录

1. #### Invalid value type for attribute 'factoryBeanObjectType': java.lang.String：通过调试发现是bean在创建的时候的问题，最后在网上找到了解决办法[4]，主要是由于 mybatis-plus 中 [mybatis](https://so.csdn.net/so/search?q=mybatis&spm=1001.2101.3001.7020) 的整合包版本不够导致的，排除 mybatis-plus 中自带的 mybatis 整合包，单独引入即可。

1. #### CORS error：在请求header中加上Authorization并放入token，请求被拦截，原因是自定义请求头后，该请求为非简单请求，而由于后端未设置具体的字段许可名单，导致该请求在预检阶段被拦截，解决办法[5] 为后端设置一下即可

1. #### Redux数据丢失：Redux的数据在刷新页面后便会丢失，需要额外在本地进行持久化处理

1. #### 前端Number精度丢失：在前端JS中，Number的存储为8个字节，而后端雪花算法算出的id值有19为，传到前端时会导致精度丢失，故id传回前端时使用String类型

1. #### Mysql Join：在实际项目开发中，不推荐使用join，因此多表联合查询在后端用代码逻辑实现

1. #### CSS污染：在编写前端代码时，在嵌套的前端组件编写时中不严谨的使用了相同的类名，导致样式出现覆盖，解决方法为在react中自动生成的webpack文件中，使用CSS-module可以消除全局污染的问题，只对引用的目录下的样式文件进行编译。



## 参考文档

[1] 阿里巴巴 JAVA 开发手册

[2] 一文搞懂什么是RESTful API - 知乎：https://zhuanlan.zhihu.com/p/334809573

[3] ENCRYPTED EXAMINATION PAPER  DISTRIBUTION SYSTEM FOR PREVENTING PAPER  LEAKAGE

[4] springboot3.2 整合 mybatis-plus - CSDN： https://blog.csdn.net/qq_24330181/article/details/134641250

[5] 向请求头添加自定义属性，实现token功能时，出现CORS跨域导致请求失败：https://blog.csdn.net/qq_41359758/article/details/115031701