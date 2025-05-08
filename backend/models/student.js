'use strict';
const { Model } = require('sequelize');

module.exports = (sequelize, DataTypes) => {
  class Student extends Model {
    static associate(models) {
      // Define association here
      Student.hasMany(models.Grade, {
        foreignKey: 'studentId',
        as: 'grades',
        onDelete: 'CASCADE', // This is important for cascade delete
        onUpdate: 'CASCADE'
      });
    }
  }
  
  Student.init({
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    name: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    age: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    class: {
      type: DataTypes.STRING,
      allowNull: false,
    },
  }, {
    sequelize,
    modelName: 'Student',
  });
  
  return Student;
};