/**
 * EnumerationPurseTypesTest.swift
 *
 * Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
 * https://github.com/intere/generator-json-models
 *
 * The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
 */

import XCTest
@testable import Generated

class EnumerationPurseTypesTest: XCTestCase {

    override func setUp() {
        super.setUp()
    }

    override func tearDown() {
        super.tearDown()
    }
}

// MARK: - subtitle Tests

extension EnumerationPurseTypesTest {

    func testSubtitleFromMap() {
        guard let model = EnumerationPurseTypes.fromMap(["subtitle": "At the time, my life just seemed too complete, and maybe we have to break everything to make something better out of ourselves."]) else {
            XCTFail("The EnumerationPurseTypes was nil")
            return
        }

        XCTAssertEqual("At the time, my life just seemed too complete, and maybe we have to break everything to make something better out of ourselves.", model.subtitle, "The subtitle is incorrect")
    }

    func testSubtitleFromEmptyMap() {
        guard let model = EnumerationPurseTypes.fromMap([:]) else {
            XCTFail("The EnumerationPurseTypes was nil")
            return
        }

        XCTAssertNil(model.subtitle, "The subtitle is not nil")
    }
}

// MARK: - title Tests

extension EnumerationPurseTypesTest {

    func testTitleFromMap() {
        guard let model = EnumerationPurseTypes.fromMap(["title": "You know how they say you only hurt the ones you love? Well, it works both ways."]) else {
            XCTFail("The EnumerationPurseTypes was nil")
            return
        }

        XCTAssertEqual("You know how they say you only hurt the ones you love? Well, it works both ways.", model.title, "The title is incorrect")
    }

    func testTitleFromEmptyMap() {
        guard let model = EnumerationPurseTypes.fromMap([:]) else {
            XCTFail("The EnumerationPurseTypes was nil")
            return
        }

        XCTAssertNil(model.title, "The title is not nil")
    }
}

