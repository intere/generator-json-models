/**
 * EnumerationTournamentStatesTest.swift
 *
 * Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import XCTest
@testable import Generated

class EnumerationTournamentStatesTest: XCTestCase {

    override func setUp() {
        super.setUp()
    }

    override func tearDown() {
        super.tearDown()
    }
}

// MARK: - name Tests

extension EnumerationTournamentStatesTest {

    func testNameFromMap() {
        guard let model = EnumerationTournamentStates.fromMap(["name": "I wanted to destroy everything beautiful I'd never have."]) else {
            XCTFail("The EnumerationTournamentStates was nil")
            return
        }

        XCTAssertEqual("I wanted to destroy everything beautiful I'd never have.", model.name, "The name is incorrect")
    }

    func testNameFromEmptyMap() {
        guard let model = EnumerationTournamentStates.fromMap([:]) else {
            XCTFail("The EnumerationTournamentStates was nil")
            return
        }

        XCTAssertNil(model.name, "The name is not nil")
    }
}

// MARK: - value Tests

extension EnumerationTournamentStatesTest {

    func testValueFromMap() {
        guard let model = EnumerationTournamentStates.fromMap(["value": 5765504458505027584]) else {
            XCTFail("The EnumerationTournamentStates was nil")
            return
        }

        XCTAssertEqual(5765504458505027584, model.value, "The value is incorrect")
    }

    func testValueFromEmptyMap() {
        guard let model = EnumerationTournamentStates.fromMap([:]) else {
            XCTFail("The EnumerationTournamentStates was nil")
            return
        }

        XCTAssertNil(model.value, "The value is not nil")
    }
}

