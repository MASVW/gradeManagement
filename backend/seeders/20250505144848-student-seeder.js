'use strict';

/** @type {import('sequelize-cli').Seeder} */
module.exports = {
  async up(queryInterface, Sequelize) {
    await queryInterface.bulkInsert('Students', [
      {
        name: 'Alice Johnson',
        age: 16,
        class: '10A',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'Bob Williams',
        age: 17,
        class: '11B',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'Charlie Brown',
        age: 15,
        class: '9C',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        name: 'Diana Smith',
        age: 16,
        class: '10A',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ], {});
  },

  async down(queryInterface, Sequelize) {
    await queryInterface.bulkDelete('Students', null, {});
  }
};
