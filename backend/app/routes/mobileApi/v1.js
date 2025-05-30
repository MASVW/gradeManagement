const StudentController = require('../../controllers/api/StudentController.js');
const SubjectController = require('../../controllers/api/SubjectController.js');
const GradeController = require('../../controllers/api/GradeController.js');
const AuthController = require('../../controllers/api/AuthController.js');
const convertToCSVController = require('../../controllers/api/CsvController.js');

const authRoutes = [
    { method: 'POST', path: '/api/v1/auth/register', handler: AuthController.register },
    { method: 'POST', path: '/api/v1/auth/login', handler: AuthController.login },
    { method: 'POST', path: '/api/v1/auth/logout', handler: AuthController.logout },
]

const studentRoutes = [
    { method: 'GET', path: '/api/v1/students', handler: StudentController.getAll },
    { method: 'GET', path: '/api/v1/students/{id}', handler: StudentController.getById },
    { method: 'POST', path: '/api/v1/students', handler: StudentController.create },
    { method: 'PUT', path: '/api/v1/students/{id}', handler: StudentController.update },
    { method: 'DELETE', path: '/api/v1/students/{id}', handler: StudentController.delete },
];

const subjectRoutes = [
    { method: 'GET', path: '/api/v1/subject', handler: SubjectController.getAll },
    { method: 'GET', path: '/api/v1/subject/{id}', handler: SubjectController.getById },
    { method: 'POST', path: '/api/v1/subject', handler: SubjectController.create },
    { method: 'PUT', path: '/api/v1/subject/{id}', handler: SubjectController.update },
    { method: 'DELETE', path: '/api/v1/subject/{id}', handler: SubjectController.delete },
];

const gradeRoutes = [
    { method: 'GET', path: '/api/v1/grades', handler: GradeController.getAll },
    { method: 'GET', path: '/api/v1/grades/{id}', handler: GradeController.getById },
    { method: 'POST', path: '/api/v1/grades', handler: GradeController.create },
    { method: 'PUT', path: '/api/v1/grades/{id}', handler: GradeController.update },
    { method: 'DELETE', path: '/api/v1/grades/{id}', handler: GradeController.delete },
];

const convertToCSVRoutes = [
    { method: 'GET', path: '/api/v1/csv/student', handler: convertToCSVController.studentToCsv },
    { method: 'GET', path: '/api/v1/csv/subject', handler: convertToCSVController.subjectToCsv },
    { method: 'GET', path: '/api/v1/csv/grade', handler: convertToCSVController.gradeToCsv },
];

module.exports = [
    {
        method: 'GET',
        path: '/',
        handler: (request, h) => 'Hello Sam!',
    },
    ...authRoutes,
    ...studentRoutes,
    ...subjectRoutes,
    ...gradeRoutes,
    ...convertToCSVRoutes
];
