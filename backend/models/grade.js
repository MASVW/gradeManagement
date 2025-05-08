'use strict';
const { Model } = require('sequelize');

module.exports = (sequelize, DataTypes) => {
  class Grade extends Model {
    static associate(models) {
      Grade.belongsTo(models.Student, {
        foreignKey: 'studentId',
        as: 'student'
      });
      
      Grade.belongsTo(models.Subject, {
        foreignKey: 'subjectId',
        as: 'subject'
      });
    }
  }
  
  Grade.init({
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    studentId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    subjectId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    score: {
      type: DataTypes.INTEGER,
      allowNull: false,
      validate: {
        min: 0,
        max: 100
      }
    },
  }, {
    sequelize,
    modelName: 'Grade',
  });
  
  return Grade;
};