/**
 * EnumerationTest.swift
 *
 * Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import XCTest
@testable import Generated

class EnumerationTest: XCTestCase {

    override func setUp() {
        super.setUp()
    }

    override func tearDown() {
        super.tearDown()
    }
}

// MARK: - purseTypes Tests

extension EnumerationTest {

    func testPurseTypesFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["purseTypes": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.purseTypes, "The purseTypes was nil")
        XCTAssertEqual(2, model.purseTypes?.count, "The purseTypes has the wrong number of objects")
    }

    func testPurseTypesFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.purseTypes, "The purseTypes is not nil")
    }
}

// MARK: - skillLevels Tests

extension EnumerationTest {

    func testSkillLevelsFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["skillLevels": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.skillLevels, "The skillLevels was nil")
        XCTAssertEqual(2, model.skillLevels?.count, "The skillLevels has the wrong number of objects")
    }

    func testSkillLevelsFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.skillLevels, "The skillLevels is not nil")
    }
}

// MARK: - tournamentSizes Tests

extension EnumerationTest {

    func testTournamentSizesFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["tournamentSizes": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.tournamentSizes, "The tournamentSizes was nil")
        XCTAssertEqual(2, model.tournamentSizes?.count, "The tournamentSizes has the wrong number of objects")
    }

    func testTournamentSizesFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.tournamentSizes, "The tournamentSizes is not nil")
    }
}

// MARK: - tournamentStates Tests

extension EnumerationTest {

    func testTournamentStatesFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["tournamentStates": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.tournamentStates, "The tournamentStates was nil")
        XCTAssertEqual(2, model.tournamentStates?.count, "The tournamentStates has the wrong number of objects")
    }

    func testTournamentStatesFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.tournamentStates, "The tournamentStates is not nil")
    }
}

// MARK: - tournamentTypes Tests

extension EnumerationTest {

    func testTournamentTypesFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["tournamentTypes": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.tournamentTypes, "The tournamentTypes was nil")
        XCTAssertEqual(2, model.tournamentTypes?.count, "The tournamentTypes has the wrong number of objects")
    }

    func testTournamentTypesFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.tournamentTypes, "The tournamentTypes is not nil")
    }
}

// MARK: - userTypes Tests

extension EnumerationTest {

    func testUserTypesFromMap() {
        let arrayData = [[:], [:]]  // Array of 2 empty objects
        guard let model = Enumeration.fromMap(["userTypes": arrayData ]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNotNil(model.userTypes, "The userTypes was nil")
        XCTAssertEqual(2, model.userTypes?.count, "The userTypes has the wrong number of objects")
    }

    func testUserTypesFromEmptyMap() {
        guard let model = Enumeration.fromMap([:]) else {
            XCTFail("The Enumeration was nil")
            return
        }

        XCTAssertNil(model.userTypes, "The userTypes is not nil")
    }
}

